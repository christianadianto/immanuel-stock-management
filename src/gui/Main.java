package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import db.access.Connect;
import utility.TableHelper;

public class Main extends JFrame implements ActionListener, MouseListener {
	
	JMenuBar menubar;
	JMenu menuGoods; 
	JMenuItem menuItemViewGoods;
	JMenu menuTransaction;
	JMenuItem menuItemViewTransactions;
	JPanel panelTable, panelSearchRight, panelEmpty, panelSearchContainer, panelGoodsContainer,
	panelFormInsert, panelFormUpdate, panelFormDelete, panelFormContainer;
	JTextField txtSearch, txtNameInsert, txtQuantityInsert, txtIDUpdate, txtPriceInsert,
	txtNameUpdate, txtPriceUpdate, txtQuantityUpdate , txtIDDelete, txtNameDelete;
	JLabel lblSearch, lblNameInsert, lblQuantityInsert, lblIDUpdate, lblPriceInsert,
	lblNameUpdate, lblQuantityUpdate, lblPriceUpdate, lblIDDelete, lblNameDelete;
	JButton btnSearch, btnInsert, btnUpdate, btnDelete;
	JTable tableItem;
	DefaultTableModel dtm;
	JScrollPane jspane;
	final int FRAME_WIDTH = 1000;
	final int FRAME_HEIGHT = 600;
	final int TABLE_WIDTH = 950;
	final int TABLE_HEIGHT = 300;
	
	Connect con;
	
	void initComponent() {
		menubar = new JMenuBar();
		menuGoods = new JMenu("Barang");
		menuItemViewGoods = new JMenuItem("Lihat semua barang"); 
		menuTransaction = new JMenu("Transaksi");
		menuItemViewTransactions = new JMenuItem("Lihat transaksi");
		panelTable = new JPanel();
		
		panelEmpty = new JPanel();
		panelGoodsContainer = new JPanel(new BorderLayout());
		panelSearchRight = new JPanel();
		panelSearchContainer = new JPanel(new BorderLayout());
		
		txtSearch = new JTextField();
		btnSearch = new JButton("Search");
		lblSearch = new JLabel("Cari Barang");
		
		panelFormInsert = new JPanel(new GridLayout(4,2));
		panelFormUpdate = new JPanel(new GridLayout(5,2));
		panelFormDelete = new JPanel(new GridLayout(3,2));
		panelFormContainer = new JPanel(new GridLayout(1,3,5,10));
		
		lblNameInsert = new JLabel("Nama Barang");
		lblQuantityInsert = new JLabel("Stok");
		lblPriceInsert = new JLabel("Harga Modal");
		
		lblIDUpdate = new JLabel("ID");
		lblNameUpdate = new JLabel("Nama Barang");
		lblQuantityUpdate = new JLabel("Stok");
		lblPriceUpdate = new JLabel("Harga Modal");
		
		lblIDDelete = new JLabel("ID");
		lblNameDelete = new JLabel("Nama Barang");
		
		btnInsert = new JButton("Insert");
		btnUpdate = new JButton("Update");
		btnDelete = new JButton("Delete");
		
		txtNameInsert = new JTextField();
		txtQuantityInsert = new JTextField();
		txtPriceInsert = new JTextField();
		
		txtIDUpdate = new JTextField();
		txtNameUpdate = new JTextField();
		txtQuantityUpdate = new JTextField();
		txtPriceUpdate = new JTextField();
		
		txtIDDelete = new JTextField();
		txtNameDelete = new JTextField();
		
		txtIDUpdate.setEnabled(false);
		txtIDDelete.setEnabled(false);
		txtNameDelete.setEnabled(false);
		
		menuGoods.add(menuItemViewGoods);
		menuTransaction.add(menuItemViewTransactions);
		
		menubar.add(menuGoods);
		menubar.add(menuTransaction);
		
		initTable();
		getData();
	
		jspane = new JScrollPane(tableItem);
		jspane.setPreferredSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));
		
		panelTable.add(jspane);
		
		panelSearchRight.add(lblSearch);
		panelSearchRight.add(txtSearch);
		panelSearchRight.add(btnSearch);
		panelSearchRight.add(panelEmpty);
		
		panelFormInsert.add(lblNameInsert);
		panelFormInsert.add(txtNameInsert);
		panelFormInsert.add(lblQuantityInsert);
		panelFormInsert.add(txtQuantityInsert);
		panelFormInsert.add(lblPriceInsert);
		panelFormInsert.add(txtPriceInsert);
		panelFormInsert.add(btnInsert);
		
		panelFormUpdate.add(lblIDUpdate);
		panelFormUpdate.add(txtIDUpdate);
		panelFormUpdate.add(lblNameUpdate);
		panelFormUpdate.add(txtNameUpdate);
		panelFormUpdate.add(lblPriceUpdate);
		panelFormUpdate.add(txtPriceUpdate);
		panelFormUpdate.add(lblQuantityUpdate);
		panelFormUpdate.add(txtQuantityUpdate);
		panelFormUpdate.add(btnUpdate);
		
		panelFormDelete.add(lblIDDelete);
		panelFormDelete.add(txtIDDelete);
		panelFormDelete.add(lblNameDelete);
		panelFormDelete.add(txtNameDelete);
		panelFormDelete.add(btnDelete);
		
		panelFormInsert.setBorder(new TitledBorder("Insert"));
		panelFormUpdate.setBorder(new TitledBorder("Update"));
		panelFormDelete.setBorder(new TitledBorder("Delete"));
		
		panelFormContainer.add(panelFormInsert);
		panelFormContainer.add(panelFormUpdate);
		panelFormContainer.add(panelFormDelete);
		
		txtSearch.setColumns(20);
		txtSearch.setFont(new Font("Arial", Font.PLAIN, 22));
		panelSearchContainer.add(panelSearchRight, BorderLayout.EAST);
		
		panelGoodsContainer.add(panelSearchContainer, BorderLayout.NORTH);
		panelGoodsContainer.add(panelTable,BorderLayout.CENTER);
		panelGoodsContainer.add(panelFormContainer, BorderLayout.SOUTH);
		
		add(panelGoodsContainer, BorderLayout.CENTER);
		
	}
	
	void initTable() {
		
		String[]columns = {"Id","Barang","Stok", "Harga Modal"};
		
		dtm = new DefaultTableModel(columns, 0){
			@Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
		};
		
		tableItem = new JTable(dtm);
		
		int [] colWidth = {50,600,100,200};
		int [] alignment = {DefaultTableCellRenderer.CENTER,DefaultTableCellRenderer.CENTER,DefaultTableCellRenderer.CENTER,
				DefaultTableCellRenderer.CENTER};
		TableHelper.setTableSize(tableItem, 4, colWidth, 30);
		TableHelper.setTableAlignment(tableItem, 4, alignment);
		TableHelper.setTableFont(tableItem, "Arial", Font.PLAIN, 18, 20);	
	}
	
	void searchGoodsName(String name) {
		if(name.equals("")) {
			getData();
			return;
		}
		dtm.setRowCount(0);
		String query = "SELECT * FROM items WHERE name LIKE '%"+name+"%' AND deleted_at IS NULL";
		ResultSet rs = con.executeQuery(query);
		
		try {
			while(rs.next()) {
				Vector<String>rows = new Vector<String>();
				rows.add(Integer.toString(rs.getInt("id")));
				rows.add(rs.getString("name"));
				rows.add(Integer.toString(rs.getInt("quantity")));
				rows.add(Integer.toString(rs.getInt("price")));
				dtm.addRow(rows);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void getData() {
		dtm.setRowCount(0);
		String query = "SELECT * FROM items WHERE deleted_at IS NULL";
		ResultSet rs = con.executeQuery(query);
		
		try {
			while(rs.next()) {
				Vector<String>rows = new Vector<String>();
				rows.add(Integer.toString(rs.getInt("id")));
				rows.add(rs.getString("name"));
				rows.add(Integer.toString(rs.getInt("quantity")));
				rows.add(Integer.toString(rs.getInt("price")));
				dtm.addRow(rows);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	void initFrame() {
		setTitle("Immanuel Stocking Management");
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		initComponent();
		setJMenuBar(menubar);
		
		menuItemViewTransactions.addActionListener(this);
		menuItemViewGoods.addActionListener(this);
		btnSearch.addActionListener(this);
		btnInsert.addActionListener(this);
		tableItem.addMouseListener(this);
		btnUpdate.addActionListener(this);
		btnDelete.addActionListener(this);
		
		setVisible(true);
	}
	
	void insertData(){
		String name = txtNameInsert.getText();
		int quantity = Integer.parseInt(txtQuantityInsert.getText());
		int price = Integer.parseInt(txtPriceInsert.getText());
		
		String query = "INSERT INTO items(name, quantity, price) VALUES ('"+name+"',"+quantity+","+price+") ";
		con.executeUpdate(query);
		
		getData();
		System.out.println("success insert");
		txtNameInsert.setText("");
		txtPriceInsert.setText("");
		txtQuantityInsert.setText("");
		
		JOptionPane.showMessageDialog(this, "sukses tambahin barang");
	}
	
	void updateData() {
		String name = txtNameUpdate.getText();
		int price = Integer.parseInt(txtPriceUpdate.getText());
		int quantity = Integer.parseInt(txtQuantityUpdate.getText());
		int id = Integer.parseInt(txtIDUpdate.getText());
		
		String query = "UPDATE items SET name = '"+name+"', price = "+price+", quantity = "+quantity+" WHERE id = "+id;
		con.executeUpdate(query);
		
		getData();
		
		clearTextField();
		JOptionPane.showMessageDialog(this, "sukses update barang");
	}
	
	void clearTextField() {
		txtNameUpdate.setText("");
		txtPriceUpdate.setText("");
		txtQuantityUpdate.setText("");
		txtIDUpdate.setText("");
		
		txtIDDelete.setText("");
		txtNameDelete.setText("");
	}
	
	void deleteData() {
		int id = Integer.parseInt(txtIDUpdate.getText());
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		
		String query = "UPDATE items SET deleted_at = '"+dateFormat.format(date).toString()+"' WHERE id = "+id;
		con.executeUpdate(query);
		
		getData();
		
		clearTextField();
		JOptionPane.showMessageDialog(this, "sukses hapus barang");
	}
	
	public Main() {
		con = new Connect();
		initFrame();
	}

	public static void main(String[] args) {
		new Main();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == menuItemViewTransactions) {		
			setContentPane(new TransactionView());
			invalidate();
			validate();
		}
		else if(e.getSource() == menuItemViewGoods) {
			txtSearch.setText("");
			getData();
			setContentPane(panelGoodsContainer);
			invalidate();
			validate();
		}
		else if(e.getSource() == btnSearch) {
			String name = txtSearch.getText();
			searchGoodsName(name);
		}
		else if(e.getSource() == btnInsert) {
			insertData();
		}
		else if(e.getSource() == btnUpdate) {
			int option = JOptionPane.showConfirmDialog(this, "Apakah barang mau di update?", "Update items", JOptionPane.YES_NO_OPTION);
			if(option == JOptionPane.YES_OPTION) {
				updateData();
			}
		}
		else if(e.getSource() == btnDelete) {
			int option = JOptionPane.showConfirmDialog(this, "Apakah barang mau di hapus?", "Delete items", JOptionPane.YES_NO_OPTION);
			if(option == JOptionPane.YES_OPTION) {
				deleteData();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == tableItem) {
			String id = tableItem.getValueAt(tableItem.getSelectedRow(), 0).toString();
			String name = tableItem.getValueAt(tableItem.getSelectedRow(), 1).toString();
			String quantity = tableItem.getValueAt(tableItem.getSelectedRow(), 2).toString();
			String price = tableItem.getValueAt(tableItem.getSelectedRow(), 3).toString();
			
			txtIDUpdate.setText(id);
			txtNameUpdate.setText(name);
			txtQuantityUpdate.setText(quantity);
			txtPriceUpdate.setText(price);
			
			txtIDDelete.setText(id);
			txtNameDelete.setText(name);
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
