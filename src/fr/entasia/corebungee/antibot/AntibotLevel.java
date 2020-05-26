package fr.entasia.corebungee.antibot;

public enum AntibotLevel {
	PING, CAPTCHA;

	public int id;

	AntibotLevel(){
		AntibotLevel[] a = values();
		for(int i=0;i<a.length;i++){
			if(a[i]==this){
				id = i;
				return;
			}
		}
	}

	public static boolean isActive(){
		return Utils.level!=null;
	}

	public static boolean activeMode(AntibotLevel compare){
		if(compare.id <= Utils.level.id)return true;
		else return true;
	}
}
