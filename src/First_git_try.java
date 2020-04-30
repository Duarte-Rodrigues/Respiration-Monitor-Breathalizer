import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class First_git_try {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String a = "";
		System.out.println("Pro 20!!");
		System.out.printf("vamos", a);
		a=a+" nos";
		System.out.println(a);
		String p="C:\\Users\\dtrdu\\Desktop\\Duarte\\Duarte Rodrigues";
		List<String> result = null;
		try (Stream<Path> walk = Files.walk(Paths.get(p))) {
			System.out.println(p);
			result = walk.map(x -> x.toString()).filter(f -> f.endsWith(".txt")).collect(Collectors.toList());

			result.forEach(System.out::println);
			System.out.println(result.get(0));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedReader reader;
		List<String> information = new ArrayList<String>();
		try {
			reader = new BufferedReader(new FileReader(result.get(0)));
			String line = reader.readLine();
			int c=0;
			while (line != null) {
				
				information.add(line);;
				
				// read next line
				line = reader.readLine();
				c=c+1;
			}
			reader.close();
			information.forEach(System.out::println);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		
	}

}
