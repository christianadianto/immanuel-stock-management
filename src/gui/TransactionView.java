package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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

public class TransactionView extends JPanel implements MouseListener{

	JTable tableTransaction;
	DefaultTableModel dtm;
	JScrollPane jspane;
	JPanel panelTable, panelSearchRight, panelEmpty, panelSearchContainer, panelGoodsContainer,
	panelFormInsert, panelFormUpdate, panelFormDelete, panelFormContainer;
	JTextField txtSearch, txtCompanyInsert, txtQuantityInsert, txtIDUpdate, txtPriceInsert,
	txtCompanyUpdate, txtPriceUpdate, txtQuantityUpdate , txtIDDelete, txtNameDelete;
	JLabel lblSearch, lblNameInsert, lblCompanyInsert, lblQuantityInsert, lblIDUpdate, lblPriceInsert,
	lblNameUpdate, lblCompanyUpdate, lblQuantityUpdate, lblPriceUpdate, lblIDDelete, lblNameDelete;
	JButton btnSearch, btnInsert, btnUpdate, btnDelete;
	
	JComboBox<String>cbListItemsInsert, cbListItemsUpdate;
	
	final int TABLE_WIDTH = 950;
	final int TABLE_HEIGHT = 300;

	Vector<Integer>listItemIds = new Vector<Integer>();
	Vector<String>listItemNames = new Vector<String>();
	Connect con;
	
	void initComponent() {
		panelEmpty = new JPanel();
		panelSearchRight = new JPanel();
		panelSearchContainer = new JPanel(new BorderLayout());
		
		txtSearch = new JTextField();
		btnSearch = new JButton("Search");
		lblSearch = new JLabel("Cari Perusahaan");
		
		panelFormInsert = new JPanel(new GridLayout(6,2));
		panelFormUpdate = new JPanel(new GridLayout(7,2));
		panelFormContainer = new JPanel(new GridLayout(1,3,5,10));
		
		lblNameInsert = new JLabel("Nama Barang");
		lblQuantityInsert = new JLabel("Stok");
		lblPriceInsert = new JLabel("Harga Modal");
		lblCompanyInsert = new JLabel("Perusahaan");
		
		lblIDUpdate = new JLabel("ID");
		lblNameUpdate = new JLabel("Nama Barang");
		lblQuantityUpdate = new JLabel("Stok");
		lblPriceUpdate = new JLabel("Harga Modal");
		lblCompanyUpdate = new JLabel("Perusahaan");
		
		btnInsert = new JButton("Insert");
		btnUpdate = new JButton("Update");
		
		txtCompanyInsert = new JTextField();
		txtQuantityInsert = new JTextField();
		txtPriceInsert = new JTextField();
		
		txtIDUpdate = new JTextField();
		txtCompanyUpdate = new JTextField();
		txtQuantityUpdate = new JTextField();
		txtPriceUpdate = new JTextField();
		
		txtIDUpdate.setEnabled(false);
		
		cbListItemsInsert = new JComboBox<String>(listItemNames);
		cbListItemsUpdate = new JComboBox<String>(listItemNames);
		
		panelSearchRight.add(lblSearch);
		panelSearchRight.add(txtSearch);
		panelSearchRight.add(btnSearch);
		panelSearchRight.add(panelEmpty);
		
		panelFormInsert.add(lblCompanyInsert);
		panelFormInsert.add(txtCompanyInsert);
		panelFormInsert.add(lblQuantityInsert);
		panelFormInsert.add(txtQuantityInsert);
		panelFormInsert.add(lblPriceInsert);
		panelFormInsert.add(txtPriceInsert);
		panelFormInsert.add(lblNameInsert);
		panelFormInsert.add(cbListItemsInsert);
		panelFormInsert.add(btnInsert);
		
		panelFormUpdate.add(lblIDUpdate);
		panelFormUpdate.add(txtIDUpdate);
		panelFormUpdate.add(lblCompanyUpdate);
		panelFormUpdate.add(txtCompanyUpdate);
		panelFormUpdate.add(lblPriceUpdate);
		panelFormUpdate.add(txtPriceUpdate);
		panelFormUpdate.add(lblQuantityUpdate);
		panelFormUpdate.add(txtQuantityUpdate);
		panelFormUpdate.add(lblNameUpdate);
		panelFormUpdate.add(cbListItemsUpdate);
		panelFormUpdate.add(btnUpdate);
		
		panelFormInsert.setBorder(new TitledBorder("Insert"));
		panelFormUpdate.setBorder(new TitledBorder("Update"));
		
		panelFormContainer.add(panelFormInsert);
		panelFormContainer.add(panelFormUpdate);
		
		txtSearch.setColumns(20);
		txtSearch.setFont(new Font("Arial", Font.PLAIN, 22));
		panelSearchContainer.add(panelSearchRight, BorderLayout.EAST);
	}
	
	public TransactionView() {
		con = new Connect();
		
		setLayout(new BorderLayout());
		initTable();
		initComponent();
		
		panelTable = new JPanel();
		jspane = new JScrollPane(tableTransaction);
		jspane.setPreferredSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));
		
		panelTable.add(jspane);
		
		add(panelTable, BorderLayout.CENTER);
		add(panelSearchContainer, BorderLayout.NORTH);
		add(panelFormContainer, BorderLayout.SOUTH);
		
		btnSearch.addMouseListener(this);
		btnInsert.addMouseListener(this);
		btnUpdate.addMouseListener(this);
		tableTransaction.addMouseListener(this);
	}
	
	void initTable() {
		
		String[]columns = {"Id","Tanggal Transaksi","Perusahaan", "Barang", "Stok", "Harga Jual"};
		
		dtm = new DefaultTableModel(columns, 0){
			@Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
		};
		
		tableTransaction = new JTable(dtm);
		
		int [] colWidth = {50,200,250,200,100,150};
		int [] alignment = {DefaultTableCellRenderer.CENTER, DefaultTableCellRenderer.CENTER,
				DefaultTableCellRenderer.CENTER, DefaultTableCellRenderer.CENTER, 
				DefaultTableCellRenderer.CENTER, DefaultTableCellRenderer.CENTER};
		
		TableHelper.setTableSize(tableTransaction, 6, colWidth, 30);
		TableHelper.setTableAlignment(tableTransaction, 6, alignment);
		TableHelper.setTableFont(tableTransaction, "Arial", Font.PLAIN, 18, 20);
		
		getData();
		
	}
	
	void searchCompany(String name) {
		if(name.equals("")) {
			getData();
			return;
		}
		dtm.setRowCount(0);
		String query = "Select ht.id, ht.transaction_date, ht.company_name, i.name, dt.quantity, dt.sell_price"
				+ " FROM header_transaction ht, detail_transaction dt, items i WHERE ht.id = dt.transaction_id AND dt.item_id = i.id "
				+ "AND company_name LIKE '%"+name+"%' ORDER BY ht.transaction_date DESC";
		ResultSet rs = con.executeQuery(query);
		
		try {
			while(rs.next()) {
				Vector<String>rows = new Vector<String>();
				rows.add(Integer.toString(rs.getInt("id")));
				rows.add(rs.getString("transaction_date"));
				rows.add(rs.getString("company_name"));
				rows.add(rs.getString("name"));
				rows.add(Integer.toString(rs.getInt("quantity")));
				rows.add(Integer.toString(rs.getInt("sell_price")));
				dtm.addRow(rows);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void insertData(){
		String name = txtCompanyInsert.getText();
		int quantity = Integer.parseInt(txtQuantityInsert.getText());
		int price = Integer.parseInt(txtPriceInsert.getText());
		
		String query = "INSERT INTO items(name, quantity, price) VALUES ('"+name+"',"+quantity+","+price+") ";
		con.executeUpdate(query);
		
		getData();
		txtCompanyInsert.setText("");
		txtPriceInsert.setText("");
		txtQuantityInsert.setText("");
		
		JOptionPane.showMessageDialog(this, "sukses tambahin transaksi");
	}
	
	void updateData() {
		String name = txtCompanyUpdate.getText();
		int price = Integer.parseInt(txtPriceUpdate.getText());
		int quantity = Integer.parseInt(txtQuantityUpdate.getText());
		int id = Integer.parseInt(txtIDUpdate.getText());
		
		String query = "UPDATE items SET name = '"+name+"', price = "+price+", quantity = "+quantity+" WHERE id = "+id;
		con.executeUpdate(query);
		
		getData();
		
		clearTextField();
		JOptionPane.showMessageDialog(this, "sukses update transaksi");
	}
	
	void clearTextField() {
		txtCompanyUpdate.setText("");
		txtPriceUpdate.setText("");
		txtQuantityUpdate.setText("");
		txtIDUpdate.setText("");
	}
	
	void getData() {
		dtm.setRowCount(0);
		
		String query = "Select ht.id, ht.transaction_date, ht.company_name, i.name, dt.quantity, dt.sell_price"
				+ " FROM header_transaction ht, detail_transaction dt, items i WHERE ht.id = dt.transaction_id AND dt.item_id = i.id "
				+ "ORDER BY ht.transaction_date DESC";
		ResultSet rs = con.executeQuery(query);
		
		try {
			while(rs.next()) {
				Vector<String>rows = new Vector<String>();
				rows.add(Integer.toString(rs.getInt("id")));
				rows.add(rs.getString("transaction_date"));
				rows.add(rs.getString("company_name"));
				rows.add(rs.getString("name"));
				rows.add(Integer.toString(rs.getInt("quantity")));
				rows.add(Integer.toString(rs.getInt("sell_price")));
				dtm.addRow(rows);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		query = "SELECT id, name FROM items";
		rs = con.executeQuery(query);
		
		listItemIds.clear();
		listItemNames.clear();
		
		try {
			while(rs.next()) {
				listItemIds.add(rs.getInt("id"));
				listItemNames.add(rs.getString("name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == btnSearch) {
			String name = txtSearch.getText();
			searchCompany(name);
		}
		else if(e.getSource() == btnInsert) {
			System.out.println(cbListItemsInsert.getSelectedIndex());
			//insertData();
		}
		else if(e.getSource() == btnUpdate) {
			int option = JOptionPane.showConfirmDialog(this, "Apakah barang mau di update?", "Update items", JOptionPane.YES_NO_OPTION);
			if(option == JOptionPane.YES_OPTION) {
				updateData();
			}
		}
		else if(e.getSource() == tableTransaction) {
			String id = tableTransaction.getValueAt(tableTransaction.getSelectedRow(), 0).toString();
			String date = tableTransaction.getValueAt(tableTransaction.getSelectedRow(), 1).toString();
			String company = tableTransaction.getValueAt(tableTransaction.getSelectedRow(), 2).toString();
			String item = tableTransaction.getValueAt(tableTransaction.getSelectedRow(), 3).toString();
			String quantity = tableTransaction.getValueAt(tableTransaction.getSelectedRow(), 4).toString();
			String price = tableTransaction.getValueAt(tableTransaction.getSelectedRow(), 5).toString();
			
			for (int i=0;i<listItemNames.size();i++) {
				if(listItemNames.get(i).equals(item)) {
					cbListItemsUpdate.setSelectedItem(item);
				}
			}
			
			txtIDUpdate.setText(id);
			txtCompanyUpdate.setText(company);
			txtQuantityUpdate.setText(quantity);
			txtPriceUpdate.setText(price);

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
