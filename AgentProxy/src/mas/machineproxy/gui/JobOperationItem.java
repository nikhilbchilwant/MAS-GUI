package mas.machineproxy.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import javax.swing.JButton;

import mas.jobproxy.job;
import mas.util.TableUtil;

public class JobOperationItem extends JButton {

	private static final long serialVersionUID = 1L;
	private int cWidth;
	private int cHeight = 150;
	public static final int padding = 3;
	public static Color boxColor = new Color(192,192,192);
	private job j;

	public JobOperationItem() {
	}

	public JobOperationItem(job job) {
		this.j = job;	
	}

	public void setDisplay(job j) {
		this.j = j;
		revalidate();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D graphics = (Graphics2D) g.create();

		graphics.setColor(getBackground());
		graphics.setFont(TableUtil.font);

		cWidth = getWidth();
		graphics.translate(padding, padding);
		// draw component
		graphics.setFont(TableUtil.font);
		graphics.fillRect(0 , 0,
				cWidth - padding, cHeight);

		graphics.setColor(Color.BLACK);
		graphics.setFont(TableUtil.headings);
		graphics.translate(cWidth/2, 0);
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		String toDisp = j.getJobID() + "abc";
		
		AffineTransform affinetransform = new AffineTransform();     
		FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
		int textwidth = (int)(TableUtil.headings.getStringBounds(toDisp, frc).getWidth());
		int textheight = (int)(TableUtil.headings.getStringBounds(toDisp, frc).getHeight());
		
		graphics.drawString( toDisp,
				0 - textwidth/2 , cHeight/2 - textheight/2);
		
		graphics.dispose();

		setPreferredSize(new Dimension(cWidth, cHeight + 2* padding));
	}
}
