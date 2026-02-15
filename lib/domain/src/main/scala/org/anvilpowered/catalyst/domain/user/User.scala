package org.anvilpowered.catalyst.domain.user

import cats.Functor
import cats.data.OptionT
import cats.effect.Async
import cats.effect.Resource
import cats.syntax.all.*
import org.anvilpowered.catalyst.domain.user.User.Email
import org.anvilpowered.catalyst.domain.user.User.Id
import org.anvilpowered.catalyst.domain.user.User.Username
import skunk.Codec
import skunk.Decoder
import skunk.Query
import skunk.Session
import skunk.codec.all.*
import skunk.implicits.*

import java.util.UUID
import skunk.Command
import skunk.data.Completion.Insert

case class User(
    id: Id,
    username: Username,
    email: Option[Email],
)

object User {
  case class Id(value: UUID) extends AnyVal
  case class Username(value: String) extends AnyVal
  case class Email(value: String) extends AnyVal

  given id: Codec[Id] = uuid.imap(Id(_))(_.value)
  given username: Codec[Username] = varchar.imap(Username(_))(_.value)
  given email: Codec[Email] = varchar.imap(Email(_))(_.value)
  given codec: Codec[User] = (id *: username *: email.opt).to[User]
}

trait UserRepository[F[_]] {
  def find(id: User.Id): OptionT[F, User]
  def insert(user: User): F[Unit]
  def list: fs2.Stream[F, User]
}


class SkunkUserRepository[F[_]: Async](session: Resource[F, Session[F]]) extends UserRepository[F] {

  private val F: Async[F] = Async[F]

  private val selectById: Query[UUID, User] =
    sql"""
      SELECT id, username, email
      FROM users
      WHERE id = $uuid
    """.query(User.codec)

  private val insertCmd: Command[User] =
    sql"""
      INSERT INTO users (id, username, email)
      VALUES ${User.codec}
    """.command

  override def find(id: Id): OptionT[F, User] = OptionT {
    session.use { s =>
      for {
        pq <- s.prepare(selectById)
        result <- pq.option(id.value)
      } yield result
    }
  }

  override def insert(user: User): F[Unit] =
    session.use { s =>
      for {
        pc <- s.prepare(insertCmd)
        completion <- pc.execute(user)
        result <- completion match
          case Insert(1) => F.unit
          case other => F.raiseError(new Exception(s"Expecected Insert(1) but was $other"))
      } yield result
    }.void

  val selectAll: Query[skunk.Void, User] =
    sql"""
      SELECT id, username, email
      FROM users
    """.query(User.codec)

  override def list: fs2.Stream[F, User] =
    for {
      res <- fs2.Stream.resource(session)
      pq <- fs2.Stream.eval(res.prepare(selectAll))
      result <- pq.stream(skunk.Void, 16)
    } yield result
}
