package fr.entasia.corebungee.antibot;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQLUpdate implements Runnable {

	public static PreparedStatement ps;

	@Override
	public void run() {
		try{
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Utils.safeListSQL.put(rs.getString("name"), rs.getString("ip"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
