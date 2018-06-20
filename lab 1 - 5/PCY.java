import java.util.Scanner;
import java.util.*;
import java.io.*;
import java.lang.*;

class PCY{


	HashMap freqItems = new HashMap();
	HashMap itemCount = new HashMap();
	HashMap cand_Pairs = new HashMap();
	HashMap cand_Triple = new HashMap();

	Hashtable<Integer, Integer> pass1_pair = new Hashtable<Integer, Integer>();
	Hashtable<Integer, Integer> pass2_pair = new Hashtable<Integer, Integer>();

	Hashtable<Integer, Integer> pass1_triple = new Hashtable<Integer, Integer>();
	Hashtable<Integer, Integer> pass2_triple = new Hashtable<Integer, Integer>();

	BitSet bm1 = new BitSet(956);
	BitSet bm2 = new BitSet(24);

	BitSet bm1_tri = new BitSet(950);
	BitSet bm2_tri = new BitSet(60);

	ArrayList<String> list = new ArrayList<String>();

	public void Pass_One(String filePath){

		try{
			File file = new File(filePath);
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()){
				String line = sc.nextLine();
				list.add(line);
				String[] ele = line.split(" ");
				int ele_size = ele.length;
				//count for frequent items
				for(int i = 0; i < ele_size; i++){
					if(itemCount.containsKey(ele[i])){
						int temp = (Integer)itemCount.get(ele[i]);
						itemCount.replace(ele[i], new Integer(temp + 1));
					}else{
						itemCount.put(ele[i], new Integer(1));
					}
				}
				//Create pair
				for(int i = 0; i < (ele_size - 1); i++){
					createPair(ele[i], ele[i+1]);
				}
				//Create triple
				for(int i = 0; i < (ele_size - 2); i++){
					createTriple(ele[i], ele[i + 1], ele[i + 2]);
				}
			}
			sc.close();
		}catch (IOException e){
			e.printStackTrace();
		}

		between_Pass1();

		System.out.println(pass1_pair.size());
		System.out.println(pass2_pair.size());

		System.out.println(pass1_triple.size());
		System.out.println(pass2_triple.size());

		System.out.println(cand_Pairs.size());
		System.out.println(cand_Triple.size());
	}

	public void between_Pass1(){
		//Replace the buckets by a bit-vector
		for(Integer key1: pass1_pair.keySet()){
			int temp2 = (Integer)pass1_pair.get(key1);
			if(temp2 >= 10){
				bm1.set(key1, true);
			}else{
				bm1.set(key1, false);
			}
		}

		for(Integer key2: pass1_triple.keySet()){
			int temp3 = (Integer)pass1_triple.get(key2);
			if(temp3 >= 10){
				bm1_tri.set(key2, true);
			}else{
				bm1_tri.set(key2, false);
			}
		}

		//Find frequent Items and list them
		for(Object key: itemCount.keySet()){
			int value = (Integer)itemCount.get(key.toString());
			if(value >= 10){
				freqItems.put(key, new Integer(value));
			}
		}
		Pass_Two();
	}

	public void Pass_Two(){
		//Check for Candidate Pairs
		for(Object key1: freqItems.keySet()){
			for(Object key2: freqItems.keySet()){
				if(key1.toString() == key2.toString()){
					continue;
				}else{
					String text = key1.toString()+","+key2.toString();
					int index = (Math.abs(text.hashCode()/7))%1000;
					if(bm1.get(index) == true){
						int value = (Math.abs(text.hashCode()*13))%200;
						if(pass2_pair.containsKey(value)){
							int temp1 = (Integer)pass2_pair.get(value);
							pass2_pair.replace(value, new Integer(temp1+1));
						}else{
							pass2_pair.put(value, new Integer(1));
						}
					}
				}
			}
		}

		for(Object key1: freqItems.keySet()){
			for(Object key2: freqItems.keySet()){
				for(Object key3: freqItems.keySet()){
					if(key1.toString() == key2.toString() ||
						key1.toString() == key3.toString() || 
						key2.toString() == key3.toString()){
						continue;
					}else{
						String text = key1.toString()+","+key2.toString()+","+key3.toString();
						int index = (Math.abs(text.hashCode()/7))%1000;
						if(bm1_tri.get(index) == true){
							int value = (Math.abs(text.hashCode()*13))%200;
							if(pass2_triple.containsKey(value)){
								int temp1 = (Integer)pass2_triple.get(value);
								pass2_triple.replace(value, new Integer(temp1+1));
							}else{
								pass2_triple.put(value, new Integer(1));
							}
						}
					}					
				}
			}
		}

		between_Pass2();
	}

	public void between_Pass2(){
		//Replace the buckets by a bit-vector
		for(Integer key1: pass2_pair.keySet()){
			int temp2 = (Integer)pass2_pair.get(key1);
			if(temp2 >= 2){
				bm2.set(key1, true);
			}else{
				bm2.set(key1, false);
			}
		}

		for(Integer key2: pass2_triple.keySet()){
			int temp3 = (Integer)pass2_triple.get(key2);
			if(temp3 >= 3){
				bm2_tri.set(key2, true);
			}else{
				bm2_tri.set(key2, false);
			}
		}

		Pass_Three();
	}

	public void Pass_Three(){
		//Check for Candidate Pairs
		for(Object key1: freqItems.keySet()){
			for(Object key2: freqItems.keySet()){
				if(key1.toString() == key2.toString()){
					continue;
				}else{
					String text = key1.toString()+","+key2.toString();
					int index1 = (Math.abs(text.hashCode()/7))%1000;
					int index2 = (Math.abs(text.hashCode()*13))%200;
					if(bm1.get(index1) == true){
						if(bm2.get(index2) == true){
							countPairs(key1.toString(), key2.toString());
						}
					}
				}
			}
		}

		//Check for Candidate Triple
		for(Object key1: freqItems.keySet()){
			for(Object key2: freqItems.keySet()){
				for(Object key3: freqItems.keySet()){
					if(key1.toString() == key2.toString() ||
						key1.toString() == key3.toString() || 
						key2.toString() == key3.toString()){
						continue;
					}else{
						String text = key1.toString()+","+key2.toString()+","+key3.toString();
						int index1 = (Math.abs(text.hashCode()/7))%1000;
						int index2 = (Math.abs(text.hashCode()*13))%200;
						if(bm1_tri.get(index1) == true){
							if(bm2_tri.get(index2) == true){
								countTriples(key1.toString(), key2.toString(), key3.toString());
							}
						}
					}
				}
			}
		}
	}

	public void countTriples(String k1, String k2, String k3){
		int count = 0;
		String triple = k1+","+k2+","+k3;
		for(String line: list){
			if(line.contains(k1)){
				if(line.contains(k2)){
					if(line.contains(k3)){
						count++;
					}
				}
			}
		}
		cand_Triple.put(triple, new Integer(count));
	}

	public void countPairs(String k1, String k2){
		int count = 0;
		String pair = k1 + "," + k2;
		for(String line: list){
			if(line.contains(k1)){
				if(line.contains(k2)){
					count++;
				}
			}
		}
		cand_Pairs.put(pair, new Integer(count));
	}


	public void createPair(String k1, String k2){
		String temp = k1+","+k2;
		int key = (Math.abs(temp.hashCode()/7))%1000;

		if(pass1_pair.containsKey(key)){
			int temp1 = (Integer)pass1_pair.get(key);
			pass1_pair.replace(key, new Integer(temp1+1));
		}else{
			pass1_pair.put(key, new Integer(1));
		}
	}

	public void createTriple(String k1, String k2, String k3){
		String temp = k1+","+k2+","+k3;
		int key = (Math.abs(temp.hashCode()/7))%1000;

		if(pass1_triple.containsKey(key)){
			int temp1 = (Integer)pass1_triple.get(key);
			pass1_triple.replace(key, new Integer(temp1+1));
		}else{
			pass1_triple.put(key, new Integer(1));
		}
	}

	public static void main(String[] args){
		String filePath = args[0];
		PCY pcy = new PCY();
		pcy.Pass_One(filePath);
	}
}