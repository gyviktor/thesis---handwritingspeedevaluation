package darabolo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException {
		List<List<String>> records = beolvas(
				"F:/szakdoga/Wacom_speed/ID0003-0885_20191126152620/capture_01_20191126152620_x.csv");
		kiir(records);
		System.out.println("Done !");
	}

	protected static List<List<String>> beolvas(String name) throws IOException {
		List<List<String>> records = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(name));
		String l;
		while ((l = br.readLine()) != null) {
			String[] values = l.split(";");
			records.add(Arrays.asList(values));
		}
		br.close();
		return records;
	}

	protected static void kiir(List<List<String>> records) throws IOException {
		List<List<String>> points = new ArrayList<>();
		for (List<String> i : records) {
			/*
			 * if (i.get(0).equals("1")) { points.add(i); }
			 */
			points.add(i);
		}

		final String lineSeparator = System.getProperty("line.separator");

		StringBuilder sb = new StringBuilder();

		int x1 = Integer.parseInt(points.get(0).get(2));
		int temp_x1 = Integer.parseInt(points.get(0).get(2));
		int y1 = Integer.parseInt(points.get(0).get(3));
		int temp_y1 = Integer.parseInt(points.get(0).get(3));
		double p1 = Double.parseDouble(points.get(0).get(4));
		int j = 1;
		int index = 1;
		List<List<String>> word = new ArrayList<>();
		List<List<List<String>>> words = new ArrayList<>();

		for (List<String> point : points) {
			if (j <= points.size()) {
				int x2 = Integer.parseInt(points.get(j - 1).get(2));
				int y2 = Integer.parseInt(points.get(j - 1).get(3));
				double p2 = Double.parseDouble(points.get(j - 1).get(4));
				double s = 0;

				if (p1 > 0 && p2 == 0) {

				} else if (p1 == 0 && p2 > 0) {
					p1 = p2;
					s = Math.sqrt(Math.pow(x2 - temp_x1, 2) + Math.pow(y2 - temp_y1, 2));
					temp_x1 = x2;
				} else {
					p1 = p2;
					s = Math.sqrt(Math.pow(x2 - temp_x1, 2) + Math.pow(y2 - temp_y1, 2));
					temp_y1 = y2;
				}
				System.out.println(s);

				x1 = x2;
				y1 = y2;

				j++;

				if (s >= 5000) {
					words.add(word);
					sb.append("EventType;T;X;Y;P;Altitude;Azimuth;Distance");
					sb.append(lineSeparator);
					for (List<List<String>> w : words) {
						for (List<String> line : w) {
							for (int i = 0; i < line.size() - 1; i++) {
								sb.append(line.get(i));
								sb.append(";");
							}
							sb.append(line.get(line.size() - 1));
							sb.append(lineSeparator);
						}
						darab(index, sb);
						sb.setLength(0);
						index++;
					}
					word.clear();
					words.clear();
				} else {
					word.add(point);
				}
			}
		}
	}

	protected static void darab(int index, StringBuilder sb) throws IOException {
		switch (index) {
		case 1:
			FileWriter writer1 = new FileWriter("x1.csv");
			writer1.append(sb);
			writer1.close();
			break;
		case 2:
			FileWriter writer2 = new FileWriter("x2.csv");
			writer2.append(sb);
			writer2.close();
			break;
		case 3:
			FileWriter writer3 = new FileWriter("x3.csv");
			writer3.append(sb);
			writer3.close();
			break;
		case 4:
			FileWriter writer4 = new FileWriter("x1.csv");
			writer4.append(sb);
			writer4.close();
			break;
		case 8:
			FileWriter writer5 = new FileWriter("szo_alairas-normal_betukkel-normal_tempoval-1.csv");
			writer5.append(sb);
			writer5.close();
			break;
		case 9:
			FileWriter writer6 = new FileWriter("szo_alairas-normal_betukkel-normal_tempoval-2.csv");
			writer6.append(sb);
			writer6.close();
			break;
		case 10:
			FileWriter writer7 = new FileWriter("szo_alairas-normal_betukkel-normal_tempoval-3.csv");
			writer7.append(sb);
			writer7.close();
			break;
		case 11:
			FileWriter writer8 = new FileWriter("szo_alairas-normal_betukkel-lassu_tempoval-1.csv");
			writer8.append(sb);
			writer8.close();
			break;
		case 12:
			FileWriter writer9 = new FileWriter("szo_alairas-normal_betukkel-lassu_tempoval-2.csv");
			writer9.append(sb);
			writer9.close();
			break;
		case 13:
			FileWriter writer10 = new FileWriter("szo_alairas-normal_betukkel-lassu_tempoval-3.csv");
			writer10.append(sb);
			writer10.close();
			break;
		case 14:
			FileWriter writer11 = new FileWriter("szo_alairas-normal_betukkel-gyors_tempoval-1.csv");
			writer11.append(sb);
			writer11.close();
			break;
		case 15:
			FileWriter writer12 = new FileWriter("szo_alairas-normal_betukkel-gyors_tempoval-2.csv");
			writer12.append(sb);
			writer12.close();
			break;
		case 16:
			FileWriter writer13 = new FileWriter("szo_alairas-normal_betukkel-gyors_tempoval-3.csv");
			writer13.append(sb);
			writer13.close();
			break;
		case 17:
			FileWriter writer14 = new FileWriter("szo_alairas-nagybetukkel-normal_tempoval-1.csv");
			writer14.append(sb);
			writer14.close();
			break;
		case 18:
			FileWriter writer15 = new FileWriter("szo_alairas-nagybetukkel-normal_tempoval-2.csv");
			writer15.append(sb);
			writer15.close();
			break;
		case 19:
			FileWriter writer16 = new FileWriter("szo_alairas-nagybetukkel-normal_tempoval-3.csv");
			writer16.append(sb);
			writer16.close();
			break;
		case 20:
			FileWriter writer17 = new FileWriter("szo_alairas-nagybetukkel-lassu_tempoval-1.csv");
			writer17.append(sb);
			writer17.close();
			break;
		case 21:
			FileWriter writer18 = new FileWriter("szo_alairas-nagybetukkel-lassu_tempoval-2.csv");
			writer18.append(sb);
			writer18.close();
			break;
		case 22:
			FileWriter writer19 = new FileWriter("szo_alairas-nagybetukkel-lassu_tempoval-3.csv");
			writer19.append(sb);
			writer19.close();
			break;
		case 23:
			FileWriter writer20 = new FileWriter("szo_alairas-nagybetukkel-gyors_tempoval-1.csv");
			writer20.append(sb);
			writer20.close();
			break;
		case 24:
			FileWriter writer21 = new FileWriter("szo_alairas-nagybetukkel-gyors_tempoval-2.csv");
			writer21.append(sb);
			writer21.close();
			break;
		case 25:
			FileWriter writer22 = new FileWriter("szo_alairas-nagybetukkel-gyors_tempoval-3.csv");
			writer22.append(sb);
			writer22.close();
			break;
		case 26:
			FileWriter writer23 = new FileWriter("mondat-normal_tempoval-1.csv");
			writer23.append(sb);
			writer23.close();
			break;
		case 27:
			FileWriter writer24 = new FileWriter("mondat-lassu_tempoval-1.csv");
			writer24.append(sb);
			writer24.close();
			break;
		case 28:
			FileWriter writer25 = new FileWriter("mondat-gyors_tempoval-1.csv");
			writer25.append(sb);
			writer25.close();
			break;

		case 29:
			FileWriter writer26 = new FileWriter("szamok-normal_tempoval-1.csv");
			writer26.append(sb);
			writer26.close();
			break;
		case 30:
			FileWriter writer27 = new FileWriter("szamok-lassu_tempoval-1.csv");
			writer27.append(sb);
			writer27.close();
			break;
		case 31:
			FileWriter writer28 = new FileWriter("bal_felso_kereszt.csv");
			writer28.append(sb);
			writer28.close();
			break;
		case 32:
			FileWriter writer29 = new FileWriter("szamok-gyors_tempoval-1.csv");
			writer29.append(sb);
			writer29.close();
			break;

		}
	}
}
