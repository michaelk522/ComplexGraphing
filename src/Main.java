import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;

import static java.awt.Color.*;
import static java.lang.Thread.sleep;

public class Main {

    public static void main(String[] args) throws IOException {
        File fortranPrefunction = new File("C:\\Users\\Michael\\IdeaProjects\\ComplexGraphingColors\\src\\fortran_prefunction.f90");

        File fortranWithFunction = new File("C:\\Users\\Michael\\IdeaProjects\\ComplexGraphingColors\\src\\fortran_with_function.f90");
        if (!fortranWithFunction.createNewFile()){
            System.out.println("An error occurred creating fortranWithFunction.f90");
        }

        try {
            FileInputStream instream = new FileInputStream(fortranPrefunction);
            FileOutputStream outstream = new FileOutputStream(fortranWithFunction);

            byte[] buffer = new byte[1024];

            int length;
            /*copying the contents from input stream to
             * output stream using read and write methods
             */
            while ((length = instream.read(buffer)) > 0){
                outstream.write(buffer, 0, length);
            }

            //Closing the input/output file streams
            instream.close();
            outstream.close();

        } catch(IOException ioe){
            System.out.println("File copied unsuccessfully!!");
            ioe.printStackTrace();
        }

        Scanner input = new Scanner(System.in);
        System.out.println("Please enter your function with 'z' as the independent variable. Ex: z**2-1");
        String userFunction = input.nextLine();

        FileWriter myWriter = new FileWriter(fortranWithFunction, true);
        myWriter.write("\n y = " + userFunction +
                "\n end function f");
        myWriter.close();

        try
        {
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd src && gfortran fortran_with_function.f90 && a.exe && exit\"");
        }
        catch (Exception e)
        {
            System.out.println("Something in the cmd messed up");
            e.printStackTrace();
        }

        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        File mark = new File ("C:\\Users\\Michael\\IdeaProjects\\ComplexGraphingColors\\src\\data.txt");
        Scanner scanner = new Scanner(mark);
        String[][][] numberStrings = new String[1001][1001][2];
        double[][][] numbers = new double[1001][1001][2];

        // Makes a the String[][] from the file
        int dummy1 = 0;
        int dummy2 = 0;
        while (scanner.hasNextLine()) {
            numberStrings[dummy1][dummy2] = scanner.nextLine().trim().split("\\s+");
            dummy2++;
            if (dummy2 % 1001 == 0) {
                dummy2 = 0;
                dummy1++;
            }
        }

        // Turns the String[][] to an int[][]
        for (int j = 0; j < 1001; j ++) {
            for (int k =0; k < 1001; k ++) {
                numbers[j][k][0] = Double.parseDouble(numberStrings[j][k][0]);
                numbers[j][k][1] = Double.parseDouble(numberStrings[j][k][1]);
            }
        }

        int width = 1001;
        int height = 1001;

        // Constructs a BufferedImage of one of the predefined image types.
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);


        for (int i = 0; i < 1001; i++) {
            for (int j = 0; j < 1001; j++) {
                float H = (float) (numbers[i][j][1]/(2*Math.PI));
                float L = (float) (1-Math.pow(0.90, numbers[i][j][0]));
                float V = L + Math.min(L, 1 -L);
                float S = V==0 ? 0 : 2*(1-(L/V));
                bufferedImage.setRGB(i, j, HSBtoRGB(H, V, S));
            }
        }

        // Save as PNG
        File file = new File("Complex Graph.png");
        ImageIO.write(bufferedImage, "png", file);

        if (!fortranWithFunction.delete()){
            System.out.println("An error occurred deleting fortranWithFunction.f90");
        }
        File fortranExecutable = new File("src\\a.exe");
        if (!fortranExecutable.delete()) {
            System.out.println("An error occurred deleting a.exe");
        }

        Desktop desktop = Desktop.getDesktop();
        desktop.open(file);

    }

}