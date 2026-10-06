package pet.itpuppy.fluffyisles.commands

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.JsonOps
import me.shedaniel.autoconfig.AutoConfigClient
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.nbt.NbtIo
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.NbtUtils
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.resources.RegistryOps
import net.minecraft.world.item.ItemStack
import pet.itpuppy.fluffyisles.FluffyIsles
import pet.itpuppy.fluffyisles.collectors.ScoreboardCollector
import pet.itpuppy.fluffyisles.config.ModConfig
import pet.itpuppy.utils.command.dsl.command
import java.io.BufferedOutputStream
import java.io.DataOutput
import java.io.DataOutputStream
import java.nio.file.StandardOpenOption
import java.util.*
import kotlin.io.path.absolutePathString
import kotlin.io.path.createParentDirectories
import kotlin.io.path.outputStream
import kotlin.jvm.optionals.getOrNull

object ConfigCommand : ClientCommand {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

    override fun register() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, context ->
            dispatcher.command(FluffyIsles.MOD_ID, context) {
                opens {
                    AutoConfigClient.getConfigScreen(ModConfig::class.java, FluffyIsles.client.screen).get()
                }

                "dev item" {
                    fun <Type : Any> serializeHandItem(ops: DynamicOps<Type>): Type? {
                        val item = FluffyIsles.client.player?.mainHandItem ?: return null
                        val ops = RegistryOps.create(ops, FluffyIsles.registry)
                        val result = ItemStack.OPTIONAL_CODEC.encodeStart(ops, item)
                        result.ifError { error ->
                            FluffyIsles.message(
                                Component.literal("Failed to serialize item!").withStyle { style ->
                                    style.withHoverEvent(
                                        HoverEvent.ShowText(
                                            Component.literal(error.message())
                                        )
                                    )
                                })
                        }
                        if (result.isError) return null

                        val data = result.resultOrPartial().getOrNull()
                        if (data == null) {
                            FluffyIsles.message(Component.literal("Item data is null!"))
                            return null
                        }

                        return data
                    }

                    fun dump(type: String, writer: (DataOutput) -> Unit) {
                        val file = FabricLoader.getInstance().configDir.resolve("items")
                            .resolve(UUID.randomUUID().toString() + ".$type")
                        file.createParentDirectories()
                        val stream = file.outputStream(StandardOpenOption.CREATE_NEW)
                        DataOutputStream(BufferedOutputStream(stream)).use {
                            writer(it)
                        }
                        FluffyIsles.message(
                            Component.literal("Dumped $type to file! ('items/${file.fileName}')")
                                .withStyle {
                                    it.withHoverEvent(HoverEvent.ShowText(Component.literal(file.absolutePathString())))
                                        .withClickEvent(ClickEvent.OpenFile(file))
                                })
                    }

                    fun copy(data: String, type: String) {
                        FluffyIsles.clipboard(data)
                        FluffyIsles.message(Component.literal("Copied $type to clipboard!"))
                    }

                    then("copy", "copy json") {
                        executes {
                            val item = serializeHandItem(JsonOps.INSTANCE) ?: return@executes
                            copy(gson.toJson(item), "json")
                        }
                    }


                    "copy snbt" executes {
                        val item = serializeHandItem(NbtOps.INSTANCE) ?: return@executes
                        val compound = item.asCompound().getOrNull()
                            ?: return@executes FluffyIsles.message(Component.literal("Item data is not a compound tag!"))
                        copy(NbtUtils.structureToSnbt(compound), "snbt")
                    }

                    "dump snbt" executes {
                        val item = serializeHandItem(NbtOps.INSTANCE) ?: return@executes
                        val compound = item.asCompound().getOrNull()
                            ?: return@executes FluffyIsles.message(Component.literal("Item data is not a compound tag!"))
                        dump("snbt") {
                            it.write(NbtUtils.structureToSnbt(compound).toByteArray())
                        }
                    }

                    "dump nbt" executes {
                        val item = serializeHandItem(NbtOps.INSTANCE) ?: return@executes
                        val compound = item.asCompound().getOrNull()
                            ?: return@executes FluffyIsles.message(Component.literal("Item data is not a compound tag!"))
                        dump("nbt") {
                            NbtIo.write(compound, it)
                        }
                    }

                    then("dump", "dump json") {
                        executes {
                            val item = serializeHandItem(JsonOps.INSTANCE) ?: return@executes
                            dump("json") {
                                it.write(gson.toJson(item).toByteArray())
                            }
                        }
                    }
                }
            }
        }
    }
}