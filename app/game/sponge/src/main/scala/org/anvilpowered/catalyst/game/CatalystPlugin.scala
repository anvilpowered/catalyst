package org.anvilpowered.catalyst.game

import com.google.inject.Inject
import org.apache.logging.log4j.Logger
import org.spongepowered.api.event.lifecycle.StartedEngineEvent
import org.spongepowered.plugin.builtin.jvm.Plugin
import org.spongepowered.api.Server
import org.spongepowered.api.command.parameter.CommandContext
import org.spongepowered.api.entity.living.player.Player

@Plugin("catalyst")
class CatalystPlugin {

  @Inject
  private val logger: Logger = ???

  def onServerStart(event: StartedEngineEvent[Server], player: Player, c: CommandContext): Unit = {}
}
