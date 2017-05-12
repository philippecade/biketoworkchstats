package com.github.philippecade.biketoworkstats;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Generates some charts
 * @author xphc
 */
public class ChartsGenerator {
	
	private static final Color BTW_BLUE = new Color(60, 176, 224);
	private static final int IMAGE_WIDTH = 800;
	private static final int IMAGE_HEIGHT = 600;
	private static final int NUM_CHART_COLUMNS = 2;

	<T extends Number> BufferedImage generateChartImage(String title, String unit, List<DataPoint<T>> data) throws IOException {
		CategoryDataset dataset = createDataset(data);
		JFreeChart chart = ChartFactory.createBarChart(title, null, unit, dataset, PlotOrientation.VERTICAL, false, false, false);
		configureChart(chart);
		return chart.createBufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT);
	}

	private void configureGraphicsContext(Graphics2D g2d, Rectangle size) {
		g2d.setColor(BTW_BLUE);
		g2d.fillRect(0, 0, size.width, size.height);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	}

	/**
	 * Configures chart properties
	 * @param chart Chart to configure
	 * @return plot area to draw the chart in
	 */
	private void configureChart(JFreeChart chart) {
		chart.setBackgroundPaint(BTW_BLUE);
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundAlpha(0.0f);
		((BarRenderer)plot.getRenderer()).setSeriesPaint(0, Color.WHITE.darker());
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
	}
	
	/**
	 * Combines the given images to a larger one, spreads the images over columns.
	 * @param images
	 * @return Combined image
	 */
	BufferedImage combineImages(List<BufferedImage> images) {
		int numberOfImagesPerColumn = (int)Math.ceil((double)images.size()/NUM_CHART_COLUMNS);
		Rectangle imageSize = new Rectangle(IMAGE_WIDTH*NUM_CHART_COLUMNS, IMAGE_HEIGHT*numberOfImagesPerColumn);
		BufferedImage combined = generateImage(imageSize);
		Graphics2D g2d = combined.createGraphics();
		configureGraphicsContext(g2d, imageSize);
		int x = 0;
		int y = 0;
		int n= 0;
		for (BufferedImage image : images) {
			g2d.drawImage(image, x, y, null);
			y += image.getHeight();
			n++;
			if (n % numberOfImagesPerColumn == 0) {
				// move to next column
				x += IMAGE_WIDTH;
				y = 0;
			}
		}
		g2d.dispose();
		return combined;
	}
	
	BufferedImage generateImage(Rectangle imageSize) {
		return new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_RGB);
	}
	
	<T extends Number> CategoryDataset createDataset(List<DataPoint<T>> data) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		data.forEach(row -> dataset.addValue(row.getValue(), "Row", row.getName()));
		return dataset;
	}
	
}
