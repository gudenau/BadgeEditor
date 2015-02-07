package com.gudenau.n3ds.sm4sh.badgeeditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Equipment implements Comparable<Equipment> {

	public static final Equipment[] equipment;
	private static final BufferedImage errorImage;
	
	static {
		errorImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		
		{
			Graphics g = errorImage.getGraphics();
			g.setColor(new Color(255, 0, 255));
			g.fillRect(0, 0, 64, 64);
			g.setColor(new Color(0, 0, 0));
			g.fillRect(0, 0, 32, 32);
			g.fillRect(32, 32, 32, 32);
			g.dispose();
		}
		
		equipment = new Equipment[256];

		BufferedReader in = new BufferedReader(new InputStreamReader(
				Equipment.class.getResourceAsStream("/res/txt/equipment.txt")));

		try {
			for (String line = in.readLine(); line != null; line = in
					.readLine()) {
				try {
					if (line.isEmpty() || line.startsWith("#")) {
						continue;
					}

					String[] split = line.split(":");
					
					if (split.length != 6) {
						continue;
					}

					Type type = Type.get(Integer.parseInt(split[0], 16));
					byte id = Byte.parseByte(split[1], 16);

					equipment[id] = new Equipment(type, id, split[2], split[3], split[4], split[5]);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public final Type type;
	public final byte id;
	public final String name, level0Name, level1Name, level2Name;
	public final ImageIcon icon;
	
	public Equipment(Type type, byte id, String name, String level0Name, String level1Name,
			String level2Name) {
		this.type = type;
		this.id = id;
		this.name = name;
		this.level0Name = level0Name;
		this.level1Name = level1Name;
		this.level2Name = level2Name;

		BufferedImage icon;
		try {
			icon = ImageIO.read(Equipment.class.getResource("/res/img/"
					+ Integer.toHexString(id) + ".png"));
		} catch (Exception e) {
			icon = errorImage;
		}
		this.icon = new ImageIcon(icon);
	}

	@Override
	public String toString(){
		return name;
	}
	
	public static enum Type {
		ATTACK(0, "Attack"), DEFENCE(1, "Defence"), SPEED(2, "Speed");

		public final int id;
		public final String name;

		Type(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public static Type get(int id) {
			for (Type t : Type.values()) {
				if (t.id == id) {
					return t;
				}
			}

			return null;
		}
	}

	public static class Bonus implements Comparable<Bonus>{
		public static final Bonus[] Bonuses;
		public static final Bonus empty;

		static {
			empty = new Bonus((byte) -1, "None", "");
			
			Bonuses = new Bonus[256];

			BufferedReader in = new BufferedReader(new InputStreamReader(
					Bonus.class.getResourceAsStream("/res/txt/bounus.txt")));

			try {
				for (String line = in.readLine(); line != null; line = in
						.readLine()) {
					try {
						if (line.isEmpty() || line.startsWith("#")) {
							continue;
						}

						String[] split = line.split(":");

						if (split.length != 3) {
							continue;
						}

						byte id = Byte.parseByte(split[0], 16);
						String name = split[1];
						String effect = split[2];

						Bonuses[id] = new Bonus(id, name, effect);
					} catch (Throwable t) {
						
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		public final byte id;
		public final String name;
		public final String effect;

		public Bonus(byte id, String name, String effect) {
			this.id = id;
			this.name = name;
			this.effect = effect;
		}

		public static Bonus get(int id) {
			if (id >= Bonuses.length || id == -1) {
				return empty;
			}

			return Bonuses[id];
		}
		
		public String toString(){
			return name;
		}

		@Override
		public int compareTo(Bonus bonus) {
			if(id == -1){
				return -1;
			}else if(bonus.id == -1){
				return 1;
			}
			
			return name.compareTo(bonus.name);
		}
	}

	@Override
	public int compareTo(Equipment e) {
		return name.compareTo(e.name);
	}
}
