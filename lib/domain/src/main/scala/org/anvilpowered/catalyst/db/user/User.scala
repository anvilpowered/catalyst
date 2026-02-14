package org.anvilpowered.catalyst.db.user

import java.util.UUID
import org.anvilpowered.catalyst.db.user.User.Id
import org.anvilpowered.catalyst.db.user.User.Username
import skunk.codec.all.*
import skunk.implicits.*
import skunk.Decoder
import skunk.Codec
import org.anvilpowered.catalyst.db.user.User.Email
import cats.data.OptionT
import cats.effect.Async
import cats.effect.Resource
import skunk.Session
import skunk.Query

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

class SkunkUserRepository[F[_]: Async](table: String, session: Resource[F, Session[F]]) extends UserRepository[F] {

  private val selectById: Query[Id, User] =
    sql"""
      SELECT id, username, email
      FROM $table
      WHERE id = ${id.value}
    """.query(User.codec)

  override def find(id: Id): OptionT[F, User] =
    OptionT(
      session.use(_.prepare(selectById).flatMap(_.option(User.id))),
    )

  override def insert(user: User): F[Unit] = ???

  override def list: fs2.Stream[F, User] = ???
}
