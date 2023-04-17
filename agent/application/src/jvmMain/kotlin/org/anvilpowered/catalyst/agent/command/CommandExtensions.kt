package org.anvilpowered.catalyst.agent.command


fun String.withoutColor() = replace("&[0-9a-fklmnor]".toRegex(), "")
