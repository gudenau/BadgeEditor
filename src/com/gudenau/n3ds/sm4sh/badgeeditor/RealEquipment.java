package com.gudenau.n3ds.sm4sh.badgeeditor;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RealEquipment {
	
	public boolean isValid;
	public byte unknown0;
	public long owner;
	public short attack;
	public short defence;
	public short speed;
	public byte range;
	public byte category;
	public byte bonus;
	public byte id;
	public short unknown2;
	public byte unknown3;
	public byte level;
	public short unknown4;
	
	public RealEquipment(){
		clear();
	}
	
	public RealEquipment(byte unknown0, long owner, short attack, short defence, short speed, byte range, byte category, byte bonus, byte id, short unknown2, byte unknown3, byte level, short unknown4) {
		this.unknown0 = unknown0;
		this.owner = owner;
		this.attack = attack;
		this.defence = defence;
		this.speed = speed;
		this.range = range;
		this.category = category;
		this.bonus = bonus;
		this.id = id;
		this.unknown2 = unknown2;
		this.unknown3 = unknown3;
		this.level = level;
		this.unknown4 = unknown4;
		
		validate();
	}

	public static RealEquipment[] load(String file) throws IOException {
		RandomAccessFile in = new RandomAccessFile(file, "r");
		in.seek(0xE830);
		
		byte[] data = new byte[24];
		RealEquipment[] equipment = new RealEquipment[3000];
		
		for(int i = 0; i < 3000; i++){
			in.read(data);
			
			equipment[i] = new RealEquipment(
					data[0],
					((long)data[1] & 0xFF) | ((((long)data[2]) & 0xFF) << 8) | ((((long)data[3]) & 0xFF) << 16) | ((((long)data[4]) & 0xFF) << 24) | ((((long)data[5]) & 0xFF) << 32) | ((((long)data[6]) & 0xFF) << 40) | ((((long)data[7]) & 0xFF) << 48),
					(short)(data[8] | (data[9] << 8)),
					(short)(data[10] | (data[11] << 8)),
					(short)(data[12] | (data[13] << 8)),
					data[14],
					data[15],
					data[16],
					data[17],
					(short)(data[18] | (data[19] << 8)),
					data[20],
					data[21],
					(short)(data[22] | (data[23] << 8))
					);
		}
		
		in.close();
		
		return equipment;
	}
	
	public String toString(){
		Equipment e = Equipment.equipment[id];
		
		if(e == null | !isValid){
			return "Empty";
		}else{
			switch(level){
			case 0:
				return e.level2Name;
			case 1:
				return e.level1Name;
			case 2:
				return e.level0Name;
			default:
				return e.name;
			}
		}
	}

	public void clear() {
		unknown0 = 0;
		owner = 0;
		attack = 0;
		defence = 0;
		speed = 0;
		range = 0;
		category = -1;
		bonus = -1;
		id = 0;
		unknown2 = 0;
		unknown3 = 0;
		level = 0;
		unknown4 = 0;
		
		isValid = false;
	}

	public void copy(RealEquipment realEquipment) {
		this.unknown0 = realEquipment.unknown0;
		this.owner = realEquipment.owner;
		this.attack = realEquipment.attack;
		this.defence = realEquipment.defence;
		this.speed = realEquipment.speed;
		this.range = realEquipment.range;
		this.category = realEquipment.category;
		this.bonus = realEquipment.bonus;
		this.id = realEquipment.id;
		this.unknown2 = realEquipment.unknown2;
		this.unknown3 = realEquipment.unknown3;
		this.level = realEquipment.level;
		this.unknown4 = realEquipment.unknown4;
		this.isValid = realEquipment.isValid;
	}

	public static void save(RealEquipment[] equip, File file) throws IOException {
		RandomAccessFile out = new RandomAccessFile(file, "rw");
		out.seek(0xE830);
		
		byte[] data = new byte[24];
		for(RealEquipment e : equip){
			data[0] = e.unknown0;
			
			data[1] = (byte) (e.owner & 0xFF);
			data[2] = (byte) ((e.owner >> 8) & 0xFF);
			data[3] = (byte) ((e.owner >> 16) & 0xFF);
			data[4] = (byte) ((e.owner >> 24) & 0xFF);
			data[5] = (byte) ((e.owner >> 32) & 0xFF);
			data[6] = (byte) ((e.owner >> 40) & 0xFF);
			data[7] = (byte) ((e.owner >> 48) & 0xFF);
			
			data[8] = (byte) (e.attack & 0xFF);
			data[9] = (byte) ((e.attack >> 8) & 0xFF);
			
			data[10] = (byte) (e.defence & 0xFF);
			data[11] = (byte) ((e.defence >> 8) & 0xFF);
			
			data[12] = (byte) (e.speed & 0xFF);
			data[13] = (byte) ((e.speed >> 8) & 0xFF);
			
			data[14] = e.range;
			data[15] = e.category;
			data[16] = e.bonus;
			data[17] = e.id;
			
			data[18] = (byte) (e.unknown2 & 0xFF);
			data[19] = (byte) ((e.unknown2 >> 8) & 0xFF);
			
			data[20] = e.unknown3;
			data[21] = e.level;

			data[22] = (byte) (e.unknown4 & 0xFF);
			data[23] = (byte) ((e.unknown4 >> 8) & 0xFF);
			
			out.write(data);
		}
		
		out.close();
	}

	public void validate() {
		if(isValid = (attack != 0 | defence != 0 | speed!= 0)){
			category = (byte)Equipment.equipment[id].type.id;
		}
		
		isValid = isValid && category != -1;
		
		System.out.println(isValid + " " + category);
	}
}
