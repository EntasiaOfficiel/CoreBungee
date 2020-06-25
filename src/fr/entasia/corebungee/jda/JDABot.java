package fr.entasia.corebungee.jda;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.EnumSet;

public class JDABot {

	public static JDA jda;
	public static TextChannel ch_sanctions;

	public static void init(String token) throws Throwable {
		JDABuilder builder = new JDABuilder(token);

		builder.setDisabledCacheFlags(EnumSet.of(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE));
		builder.setBulkDeleteSplittingEnabled(false);
		builder.setCompression(Compression.ZLIB);
		jda = builder.build();

		ch_sanctions = jda.getTextChannelById("526116939555274772"); // ID rapports de sanctions
	}
}
