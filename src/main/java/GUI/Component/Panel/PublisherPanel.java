package GUI.Component.Panel;

import BUS.PublisherBUS;
import DTO.PublisherDTO;
import GUI.Component.Button.*;
import GUI.Component.Dialog.AddPublisherDialog;
import GUI.Component.Dialog.AlertDialog;
import GUI.Component.Dialog.DeletePublisherDialog;
import GUI.Component.Dialog.UpdatePublisherDialog;
import GUI.Component.Panel.Components.SearchNavBarLabel;
import GUI.Component.Table.PublisherTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PublisherPanel extends JPanel {
    private final PublisherBUS publisherBUS = new PublisherBUS();
    private final PublisherTable publisherTable = new PublisherTable();
    private ButtonAdd buttonAdd;
    private ButtonUpdate buttonUpdate;
    private ButtonDelete buttonDelete;
    private ButtonExportExcel buttonExportExcel;
    private ButtonImportExcel buttonImportExcel;
    private SearchNavBarLabel searchNavBarLabel;

    private JFrame parentFrame;

    public PublisherPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setLayout(new BorderLayout());
        this.add(buttonPanel(parentFrame), BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(publisherTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(0, 17));
        this.add(emptyPanel, BorderLayout.SOUTH);
        loadData();
    }

    public JPanel buttonPanel(JFrame parentFrame) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonAdd = new ButtonAdd();
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AddPublisherDialog addPublisherDialog = new AddPublisherDialog(parentFrame, PublisherPanel.this);
                addPublisherDialog.setVisible(true);
            }
        });
        buttonUpdate = new ButtonUpdate();
        buttonUpdate.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                PublisherDTO publisher = publisherTable.getSelectedPublisher();
                if (publisher == null) {
                    AlertDialog updateAlert = new AlertDialog(parentFrame, "Vui lòng chọn nhà xuất bản cần sửa");
                    updateAlert.setVisible(true);
                } else {
                    UpdatePublisherDialog updatePublisherDialog = new UpdatePublisherDialog(parentFrame, PublisherPanel.this, publisher);
                    updatePublisherDialog.setVisible(true);
                }
            }
        });
        buttonDelete = new ButtonDelete();
        buttonDelete.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                PublisherDTO publisher = publisherTable.getSelectedPublisher();
                if (publisher == null) {
                    AlertDialog deleteAlert = new AlertDialog(parentFrame, "Vui lòng chọn nhà xuất bản cần xóa");
                    deleteAlert.setVisible(true);
                } else {
                    DeletePublisherDialog deletePublisherDialog = new DeletePublisherDialog(parentFrame, PublisherPanel.this, publisher);
                    deletePublisherDialog.setVisible(true);
                }
            }
        });
        buttonExportExcel = new ButtonExportExcel();
        buttonImportExcel = new ButtonImportExcel();
        searchNavBarLabel = new SearchNavBarLabel();
        buttonPanel.add(buttonAdd);
        buttonPanel.add(buttonUpdate);
        buttonPanel.add(buttonDelete);
        buttonPanel.add(buttonExportExcel);
        buttonPanel.add(buttonImportExcel);
        buttonPanel.add(Box.createRigidArea(new Dimension(60, 0)));
        buttonPanel.add(searchNavBarLabel);
        return buttonPanel;
    }

    private void loadData() {
        List<PublisherDTO> publishers = publisherBUS.getAllPublishers();
        if (publishers != null) {
            publisherTable.setPublisherList(publishers);
        } else {
            JOptionPane.showMessageDialog(this, "Không có nhà xuất bản nào trong cơ sở dữ liệu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void refreshTable() {
        publisherTable.refreshTable();
    }

    public void addPublisher(PublisherDTO publisher) {
        if (publisher != null) {
            publisherTable.addPublisher(publisher);
        }
    }

    public void updatePublisher(PublisherDTO publisher) {
        if (publisher != null) {
            publisherTable.updatePublisher(publisher);
        }
    }

    public void deletePublisher(PublisherDTO publisher) {
        if (publisher != null) {
            publisherTable.removePublisher(publisher);
        }
    }

    public PublisherDTO getSelectedPublisher() {
        return publisherTable.getSelectedPublisher();
    }
}
