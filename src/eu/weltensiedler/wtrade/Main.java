package eu.weltensiedler.wtrade;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	private ArrayList<Player> abos;
	String prefix = Messages.getString("Main.prefix"); //$NON-NLS-1$

	@Override
	public void onEnable() {
		abos = new ArrayList<Player>();
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent pqe) {
		System.out.println(Messages.getString("Main.playerQuitEvent")); //$NON-NLS-1$
		if (abos.contains(pqe.getPlayer())) {
			abos.remove(pqe.getPlayer());
		}
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent pke) {
		System.out.println(Messages.getString("Main.playerKickEvent")); //$NON-NLS-1$
		if (abos.contains(pke.getPlayer())) {
			abos.remove(pke.getPlayer());
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase(Messages.getString("Main.commandHandel")) || command.getName().equalsIgnoreCase(Messages.getString("Main.commandTrade"))) { //$NON-NLS-1$ //$NON-NLS-2$

			if (args.length == 0) {

				// Argumentenliste zu kurz. Anleitung soll an Sender ausgegeben
				// werden
				showHelp(sender);
			} else if (args.length == 1 && args[0].equalsIgnoreCase(Messages.getString("Main.subCommandAbo"))) { //$NON-NLS-1$
				if (!abos.contains(sender)) {
					// Sender befindet sich noch nicht im Channel, wird
					// eingetragen
					subscribeChannel(sender);
				} else {
					// Sender befindet sich im Channel, wird ausgetragen
					unsubscribeChannel(sender);
				}
			} else if (args.length > 1 || (!args[0].equalsIgnoreCase(Messages.getString("Main.subCommandAbo")) && args.length == 1)) { //$NON-NLS-1$
				if (abos.contains(sender) && abos.size()== 1) {
					sendNoUserInChannelMessage(sender);
				} else {
					if (!abos.contains(sender)) {
						sendNotInChannelMessage(sender);
					} else {
						sendMessage(args, sender);
					}
				}
			}
		}
		return true;
	}

	public boolean showHelp(CommandSender sender) {
		sender.sendMessage(prefix
				+ Messages.getString("Main.showHelp")); //$NON-NLS-1$
		return true;
	}

	public boolean subscribeChannel(CommandSender sender) {

		sender.sendMessage(prefix + Messages.getString("Main.channelSubscribed")); //$NON-NLS-1$
		for (Player k : abos) {
			try {
				k.sendMessage(prefix + Messages.getString("Main.yellow") + sender.getName() + Messages.getString("Main.channelJoined")); //$NON-NLS-1$ //$NON-NLS-2$
			} catch (Exception ex) {
				abos.remove(k);
			}
		}
		abos.add((Player) sender);
		return true;
	}

	public boolean unsubscribeChannel(CommandSender sender) {
		abos.remove(sender);
		sender.sendMessage(prefix + Messages.getString("Main.channelUnsubscribed")); //$NON-NLS-1$
		for (CommandSender k : abos) {
			try {
				k.sendMessage(prefix + Messages.getString("Main.yellow") + sender.getName() + Messages.getString("Main.channelLeft")); //$NON-NLS-1$ //$NON-NLS-2$
			} catch (Exception ex) {
				abos.remove(k);
			}
		}
		return true;
	}

	public boolean sendNoUserInChannelMessage(CommandSender sender) {
		sender.sendMessage(prefix + Messages.getString("Main.noSubscribers")); //$NON-NLS-1$
		return true;
	}

	public boolean sendNotInChannelMessage(CommandSender sender) {
		sender.sendMessage(
				prefix + Messages.getString("Main.notSubscribed")); //$NON-NLS-1$
		return true;
	}

	public boolean sendMessage(String[] args, CommandSender sender) {

		String message = Messages.getString("Main.yellow") + sender.getName() + Messages.getString("Main.resetItalic"); //$NON-NLS-1$ //$NON-NLS-2$
		for (String s : args) {
			message += Messages.getString("Main.space") + s; //$NON-NLS-1$
		}
		for (Player k : abos) {
			try {
				k.sendMessage(prefix + message);
			} catch (Exception ex) {
				abos.remove(k);
			}

		}
		return true;

	}
}