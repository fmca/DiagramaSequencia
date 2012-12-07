import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
public class ShowImage extends Panel {
  BufferedImage  image;
  public ShowImage(String imagem) {
  try {
  File input = new File(imagem);
  image = ImageIO.read(input);
  } catch (IOException ie) {
  System.out.println("Error:"+ie.getMessage());
  }
  }

  public void paint(Graphics g) {
  g.drawImage( image, 0, 0, null);
  }
  
  public void mostrarImagem(String nomeDiagrama){
	  JFrame frame = new JFrame(nomeDiagrama);
	  Panel panel = new ShowImage("out.png");
	  frame.getContentPane().add(panel);
	  frame.setSize(500, 500);
	  frame.setVisible(true);
  }

}
