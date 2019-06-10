package utility;

import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class TableHelper {

	public TableHelper() {
		// TODO Auto-generated constructor stub
	}
	
	public static void setTableSize(JTable table, int colCount, int [] colWidth, int rowHeigth) {
		TableColumnModel columnModel = table.getColumnModel();
		for (int i = 0; i < colCount; i++) {
			columnModel.getColumn(i).setPreferredWidth(colWidth[i]);
		}
		
		table.setRowHeight(rowHeigth);
	}
	
	public static void setTableAlignment(JTable table, int colCount, int [] alignment) {
		TableColumnModel columnModel = table.getColumnModel();
		for (int i = 0; i < colCount; i++) {
			DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(alignment[i]);
			columnModel.getColumn(i).setCellRenderer(renderer);
		}

	}
	
	public static void setTableFont(JTable table, String font, int typographicalEmphasis, int fontBodySize, int fontHeaderSize) {
		table.setFont(new Font(font, typographicalEmphasis, fontBodySize));
		table.getTableHeader().setFont(new Font(font, typographicalEmphasis, fontHeaderSize));
	}

}
