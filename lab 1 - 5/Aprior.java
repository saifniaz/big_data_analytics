import java.util.Scanner;
import java.util.*;
import java.io.*;

class Aprior{

	HashMap hm = new HashMap();
	HashMap L1 = new HashMap();
	HashMap C2 = new HashMap();
	HashMap L2 = new HashMap();
	HashMap C3 = new HashMap();

	ArrayList<String> list = new ArrayList<String>();

	public void Pass_One(String filePath){

		//Load the data from file to hash, which stores the 
		//number of times it occurs in the file

		try{
			File file = new File(filePath);
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()){
				String line = sc.nextLine();
				list.add(line);
				String[] ele = line.split(" ");
				int ele_size = ele.length;
				//Items Counts
				for(int i = 0; i < ele_size; i++){
					if(hm.containsKey(ele[i])){
						int temp = (Integer)hm.get(ele[i]);
						hm.replace(ele[i], new Integer(temp + 1));
					}else{
						hm.put(ele[i], new Integer(1));
					}
				}
			}
			sc.close();

		}catch (IOException e){
			e.printStackTrace();
		}

		Pass_Two();
		
		System.out.println(hm.size()+", "+C2.size()+", "+C3.size());

		//C3.forEach((k,v) ->{
		//System.out.println(C2.size()+", "+L1.size()+", "+hm.size()); 
		//System.out.format("Key: %s, value: %d%n", k, v);
		//});
	}


	public void Pass_Two(){
		//Create another map called L1 that stores frequent items from hm
		for(Object key: hm.keySet()){
			int value = (Integer)hm.get(key.toString());
			if(value >= 10){
				L1.put(key, new Integer(value));
			}
		}

		//Create pair from L1 and load it at C2
		//Check for Frequent Items as I put it in C2
		for(Object key1: L1.keySet()){
			for(Object key2: L1.keySet()){
				if(key1.toString() == key2.toString()){
					continue;
				}else{
					int count1 = checkLineC2(key1.toString(), key2.toString());
					String text2 = key1.toString() +","+ key2.toString();
					C2.put(text2, new Integer(count1));
				}
			}
		}
		Pass_Three();
	}

	public void Pass_Three(){
		//Find Frequent Pairs and store it at L2
		for(Object key : C2.keySet()){
			int value = (Integer)C2.get(key.toString());
			if(value >= 20){
				L2.put(key, new Integer(value));
			}
		}

		//Create pair of three from frequent items used
		for(Object k1: L2.keySet()){
			for(Object k2: L2.keySet()){
				if(k1.toString() == k2.toString()){
					continue;
				}else {
					String[] triple1 = k1.toString().split(",");
					String[] triple2 = k2.toString().split(",");
					int c1 = checkLineC3(triple1[0], triple1[1], triple2[0]);
					String c1_str = triple1[0]+","+triple1[1]+","+triple2[0];
					int c2 = checkLineC3(triple1[0], triple1[1], triple2[1]);
					String c2_str = triple1[0]+","+triple1[1]+","+triple2[1];

					C3.put(c1_str, new Integer(c1));
					C3.put(c2_str, new Integer(c2));
				}
			}
		}
	}

	public int checkLineC1(String key_1){
		int count = 0;
		for(String line : list){
			if(line.contains(key_1)){
				count++;
			}
		}
		return count;
	}

	public int checkLineC3(String key_1, String key_2, String key_3){
		int count = 0;
		for(String line : list){
			if(line.contains(key_1)){
				if(line.contains(key_2)){
					if(line.contains(key_3)){
						count++;
					}
				}
			}else{
				continue;
			}
		}
		return count;
	}

	public int checkLineC2(String key_1, String key_2){
		int count = 0;
		for(String line : list){
			if(line.contains(key_1)){
				if(line.contains(key_2)){
					count++;
				}
			}else{
				continue;
			}
		}
		return count;
	}


	public static void main(String[] args){
		String filePath = args[0];
		Aprior aprior = new Aprior();
		aprior.Pass_One(filePath);
	}
	
}
