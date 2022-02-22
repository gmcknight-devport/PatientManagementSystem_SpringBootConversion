/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmck.PatientManagementSystem.Views;

import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Glenn McKnight
 */
public class PatientView extends javax.swing.JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5237352647920759078L;
	/**
     * Creates new form PatientView
     */
    public PatientView() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        titleJLabel = new javax.swing.JLabel();
        logoutJButton = new javax.swing.JButton();
        backgroundJTabbedPane = new javax.swing.JTabbedPane();
        homeJPanel = new javax.swing.JPanel();
        welcomeJLabel = new javax.swing.JLabel();
        userInfoJText = new javax.swing.JTextArea();
        detailsJLabel = new javax.swing.JLabel();
        currentPrescriptionJLabel = new javax.swing.JLabel();
        currentPrescriptionJText = new javax.swing.JTextField();
        messagesJLabel = new javax.swing.JLabel();
        accountTerminationJButton = new javax.swing.JButton();
        deleteMessageJButton = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        messagesTable = new javax.swing.JTable();
        appointmentsJPanel = new javax.swing.JPanel();
        yourAppointmentsJLabel = new javax.swing.JLabel();
        requestAppointmentJLabel = new javax.swing.JLabel();
        appointmentDateJSpinner = new javax.swing.JSpinner();
        appointmentDateJLabel = new javax.swing.JLabel();
        appointmentTimeJLabel = new javax.swing.JLabel();
        appointmentDOctorJLabel = new javax.swing.JLabel();
        appointmentTimeJCombo = new javax.swing.JComboBox<>();
        appointmentDoctorJCombo = new javax.swing.JComboBox<>();
        requestAppointmentJButton = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        appointmentsTable = new javax.swing.JTable();
        doctorJPanel = new javax.swing.JPanel();
        doctorsJLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        currentDocRatingsJList = new javax.swing.JList<>();
        rateDoctorJLabel = new javax.swing.JLabel();
        doctorRatingJlabel = new javax.swing.JLabel();
        doctorsDocIDJCombo = new javax.swing.JComboBox<>();
        ratingJLabel = new javax.swing.JLabel();
        ratingJSpinner = new javax.swing.JSpinner();
        feedbackJLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        feedbackJText = new javax.swing.JTextArea();
        rateJButton = new javax.swing.JButton();
        historyJPanel = new javax.swing.JPanel();
        appointmentHistoryJLabel = new javax.swing.JLabel();
        prescriptionHistoryJLabel = new javax.swing.JLabel();
        notesHistoryJLabel = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        appointmentHistoryJList = new javax.swing.JList<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        prescriptionHistoryJList = new javax.swing.JList<>();
        jScrollPane6 = new javax.swing.JScrollPane();
        notesHistoryJList = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(660, 470));
        setName("backgroundJFrame"); // NOI18N

        titleJLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        titleJLabel.setText("Indisposed Clinic");

        logoutJButton.setText("Log Out");

        welcomeJLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        welcomeJLabel.setText("Welcome Back!");

        userInfoJText.setEditable(false);
        userInfoJText.setColumns(20);
        userInfoJText.setRows(5);
        userInfoJText.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        detailsJLabel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        detailsJLabel.setText("Your details:");

        currentPrescriptionJLabel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        currentPrescriptionJLabel.setText("Current Precription ");

        currentPrescriptionJText.setEditable(false);

        messagesJLabel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        messagesJLabel.setText("Messages");

        accountTerminationJButton.setText("Request Account Termination");

        deleteMessageJButton.setText("Delete Message");

        messagesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane10.setViewportView(messagesTable);

        javax.swing.GroupLayout homeJPanelLayout = new javax.swing.GroupLayout(homeJPanel);
        homeJPanel.setLayout(homeJPanelLayout);
        homeJPanelLayout.setHorizontalGroup(
            homeJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homeJPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(accountTerminationJButton))
            .addGroup(homeJPanelLayout.createSequentialGroup()
                .addGroup(homeJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homeJPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(welcomeJLabel))
                    .addGroup(homeJPanelLayout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(homeJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(currentPrescriptionJLabel)
                            .addGroup(homeJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(homeJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(userInfoJText, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                                    .addComponent(detailsJLabel)
                                    .addComponent(currentPrescriptionJText))
                                .addGroup(homeJPanelLayout.createSequentialGroup()
                                    .addComponent(messagesJLabel)
                                    .addGap(330, 330, 330)
                                    .addComponent(deleteMessageJButton)))
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(119, Short.MAX_VALUE))
        );
        homeJPanelLayout.setVerticalGroup(
            homeJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(welcomeJLabel)
                .addGap(31, 31, 31)
                .addComponent(detailsJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userInfoJText, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messagesJLabel)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteMessageJButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addComponent(currentPrescriptionJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(currentPrescriptionJText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(accountTerminationJButton))
        );

        backgroundJTabbedPane.addTab("Home", homeJPanel);

        yourAppointmentsJLabel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        yourAppointmentsJLabel.setText("Your Appointments");

        requestAppointmentJLabel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        requestAppointmentJLabel.setText("Request Appointment");

        appointmentDateJSpinner.setModel(new javax.swing.SpinnerDateModel());
        appointmentDateJSpinner.setToolTipText("");

        appointmentDateJLabel.setText("Appointment date:");

        appointmentTimeJLabel.setText("Appointment time:");

        appointmentDOctorJLabel.setText("Doctor:");

        requestAppointmentJButton.setText("Request Appointment");

        appointmentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane9.setViewportView(appointmentsTable);

        javax.swing.GroupLayout appointmentsJPanelLayout = new javax.swing.GroupLayout(appointmentsJPanel);
        appointmentsJPanel.setLayout(appointmentsJPanelLayout);
        appointmentsJPanelLayout.setHorizontalGroup(
            appointmentsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(appointmentsJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(appointmentsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(appointmentsJPanelLayout.createSequentialGroup()
                        .addGroup(appointmentsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(requestAppointmentJLabel)
                            .addGroup(appointmentsJPanelLayout.createSequentialGroup()
                                .addGroup(appointmentsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(appointmentDateJLabel)
                                    .addComponent(appointmentTimeJLabel)
                                    .addComponent(appointmentDOctorJLabel))
                                .addGap(38, 38, 38)
                                .addGroup(appointmentsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(appointmentDoctorJCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(appointmentTimeJCombo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(appointmentDateJSpinner, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(requestAppointmentJButton))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(appointmentsJPanelLayout.createSequentialGroup()
                        .addGroup(appointmentsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(appointmentsJPanelLayout.createSequentialGroup()
                                .addComponent(yourAppointmentsJLabel)
                                .addGap(0, 540, Short.MAX_VALUE))
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        appointmentsJPanelLayout.setVerticalGroup(
            appointmentsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(appointmentsJPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(yourAppointmentsJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(requestAppointmentJLabel)
                .addGap(18, 18, 18)
                .addGroup(appointmentsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(appointmentDateJLabel)
                    .addComponent(appointmentDateJSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(appointmentsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(appointmentTimeJLabel)
                    .addComponent(appointmentTimeJCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(appointmentsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(appointmentDOctorJLabel)
                    .addComponent(appointmentDoctorJCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(requestAppointmentJButton)
                .addContainerGap(96, Short.MAX_VALUE))
        );

        backgroundJTabbedPane.addTab("Appointments", appointmentsJPanel);

        doctorsJLabel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        doctorsJLabel.setText("Doctors");

        jScrollPane1.setViewportView(currentDocRatingsJList);

        rateDoctorJLabel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        rateDoctorJLabel.setText("Rate Doctor");

        doctorRatingJlabel.setText("Doctor:");

        ratingJLabel.setText("Rating:");

        ratingJSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 5, 1));

        feedbackJLabel.setText("Feedback:");

        feedbackJText.setColumns(20);
        feedbackJText.setRows(5);
        jScrollPane2.setViewportView(feedbackJText);

        rateJButton.setText("Rate Doctor");

        javax.swing.GroupLayout doctorJPanelLayout = new javax.swing.GroupLayout(doctorJPanel);
        doctorJPanel.setLayout(doctorJPanelLayout);
        doctorJPanelLayout.setHorizontalGroup(
            doctorJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(doctorJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(doctorJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, doctorJPanelLayout.createSequentialGroup()
                        .addGroup(doctorJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(doctorJPanelLayout.createSequentialGroup()
                                .addComponent(ratingJLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ratingJSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(doctorJPanelLayout.createSequentialGroup()
                                .addGroup(doctorJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(doctorRatingJlabel)
                                    .addComponent(doctorsJLabel)
                                    .addComponent(rateDoctorJLabel)
                                    .addComponent(feedbackJLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                                .addGroup(doctorJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(doctorsDocIDJCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)))
                            .addGroup(doctorJPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(rateJButton)))
                        .addGap(190, 190, 190)))
                .addContainerGap())
        );
        doctorJPanelLayout.setVerticalGroup(
            doctorJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(doctorJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(doctorsJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(rateDoctorJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(doctorJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(doctorRatingJlabel)
                    .addComponent(doctorsDocIDJCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(doctorJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ratingJLabel)
                    .addComponent(ratingJSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(doctorJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(feedbackJLabel)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(rateJButton)
                .addContainerGap(108, Short.MAX_VALUE))
        );

        backgroundJTabbedPane.addTab("Doctors", doctorJPanel);

        appointmentHistoryJLabel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        appointmentHistoryJLabel.setText("Appointment History");

        prescriptionHistoryJLabel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        prescriptionHistoryJLabel.setText("Prescription History");

        notesHistoryJLabel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        notesHistoryJLabel.setText("Notes History");

        jScrollPane4.setViewportView(appointmentHistoryJList);

        jScrollPane5.setViewportView(prescriptionHistoryJList);

        jScrollPane6.setViewportView(notesHistoryJList);

        javax.swing.GroupLayout historyJPanelLayout = new javax.swing.GroupLayout(historyJPanel);
        historyJPanel.setLayout(historyJPanelLayout);
        historyJPanelLayout.setHorizontalGroup(
            historyJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(historyJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(historyJPanelLayout.createSequentialGroup()
                        .addComponent(notesHistoryJLabel)
                        .addGap(212, 590, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, historyJPanelLayout.createSequentialGroup()
                        .addGroup(historyJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, historyJPanelLayout.createSequentialGroup()
                                .addGroup(historyJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(appointmentHistoryJLabel)
                                    .addComponent(prescriptionHistoryJLabel))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        historyJPanelLayout.setVerticalGroup(
            historyJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(appointmentHistoryJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(prescriptionHistoryJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(notesHistoryJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(91, Short.MAX_VALUE))
        );

        backgroundJTabbedPane.addTab("History", historyJPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(titleJLabel)
                .addGap(161, 161, 161)
                .addComponent(logoutJButton)
                .addContainerGap())
            .addComponent(backgroundJTabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(logoutJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backgroundJTabbedPane))
        );

        pack();
    }// </editor-fold>                        

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PatientView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PatientView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PatientView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PatientView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PatientView().setVisible(true);
            }
        });
    }
    /**
     * Displays a JOptionPane to provide information to the user when required. 
     * @param message to be displayed.
     */
    public void displayMessage(String message){
        JOptionPane.showMessageDialog(this, message);
    }
    /**
     * Sets text box with logged in user information.
     * @param info about logged in user.
     */
    public void setUserInfoText(String info){
        userInfoJText.setText(info);
    }
    /**
   * Set the messages the user has received to the messageTable.
   * @param messages of user to be displayed.
   */
   public void setUserMessages(String[] colNames, String[][] messages){
  	 DefaultTableModel model = new DefaultTableModel(messages, colNames);
  	 messagesTable.setModel(model);
   }
  /**
   * Register button handler for delete message button.
   * @param controller to respond to event.
   */
  public void addDeleteMessageButtonHandler(ActionListener controller){
      deleteMessageJButton.addActionListener(controller);
  }
  /**
   * Get the index of the selected message to be deleted.
   * @return 
   */
   public String getMessageToDelete(){
       int row = messagesTable.getSelectedRow();
       return messagesTable.getModel().getValueAt(row, 0).toString();
   } 
  /**
   * Display the patient's current prescription.
   * @param currentPrescription to be displayed.
   */
  public void setCurrentPrescription(String currentPrescription){
      currentPrescriptionJText.setText(currentPrescription);
  }
  /**
   * Register button handler for account termination request button.
   * @param controller  to respond to event.
   */
  public void addAccountTerminationButtonHandler(ActionListener controller){
      accountTerminationJButton.addActionListener(controller);
  }
  /**
   * Sets JTable of appointments booked for user.
   * @param appointments array of appointments.
   */
  public void setAppointmentTable(String[] colNames, String[][] appointments){
  	DefaultTableModel model = new DefaultTableModel(appointments, colNames);
  	appointmentsTable.setModel(model);
  	
  	//Hide ID column - not needed for user but useful to minimise excessive code to
  	//find Appointment records.
  	appointmentsTable.removeColumn(appointmentsTable.getColumn(colNames[0]));
  }
    /**
     * Sets format of date spinner to local date to avoid confusion between 
     * different potential date formats.
     */
    public void setAppointmentDateSpinnerFormat(){       
        try{
            SimpleDateFormat format;
            
            format = ((JSpinner.DateEditor) appointmentDateJSpinner.getEditor()).getFormat();
            format.applyPattern("dd/MM/yy");
            appointmentDateJSpinner.commitEdit();
            
            
        } catch(ParseException ex){
            displayMessage("Appointment booking date format error");
        }            
    }
    /**
     * Fills JComboBox with possible appointment times.
     * @param times to be set.
     */
    public void setAppointmentTimeCombo(List<String> times){
        times.forEach((s) -> {
            appointmentTimeJCombo.addItem(s);
        });
    }
    /**
     * Set doctor JCombo box to display doctors available for appointments.
     * @param doctorIDs to be displayed.
     */
    public void setDoctorCombos(List<String> doctorIDs){
        doctorIDs.forEach((s) -> {
            appointmentDoctorJCombo.addItem(s);
            doctorsDocIDJCombo.addItem(s);
        });
    }
    /**
     * Return selected appointment date from JSpinner.
     * @return date selected.
     */    
    public LocalDate getAppointmentDate(){        
        try{              
            Date date = (Date)appointmentDateJSpinner.getValue();
            LocalDate returnDate = date.toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate();
            
            returnDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return returnDate;
            
        } catch(ClassCastException ex){
            displayMessage("Couldn't get appointment date");            
            return null;
        }
    }
    /**
     * Get selected time for appointment.
     * @return appointment time requested.
     */
    public LocalTime getAppointmentTime(){
    	LocalTime time = LocalTime.parse(appointmentTimeJCombo.getSelectedItem().toString());
        return time;
    }
    /**
     * Return the doctor which the appointment is to be booked for.
     * @return selected doctor.
     */
    public String getAppointmentDoctor(){
        return appointmentDoctorJCombo.getSelectedItem().toString();
    }
    /**
     * Display list o doctors and heir ratings for patient's information.
     * @param doctors information of who they are and their ratings.
     */
    public void setRatedDoctors(String[] doctors){
        if(doctors != null){
            currentDocRatingsJList.setListData(doctors);
        }
    }
    /**
     * Get the selected doctor to b rated.
     * @return selected doctor.
     */
    public String getRatedDoctor(){
        return doctorsDocIDJCombo.getSelectedItem().toString();
    }
    /**
     * Get the rating for the patient has given to the selected doctor.
     * @return doctor rating.
     */
    public int getDoctorRating(){
        try{
            return (int) ratingJSpinner.getValue();            
        }catch(ClassCastException ex){
            displayMessage("Couldn't get doctor rating");  
            return 1;
        }
    }
    /**
     * Get the feedback the user has left for the specified doctor.
     * @return 
     */
    public String getFeedback(){
        return feedbackJText.getText();
    }
    /**
     * Clear feedback text box so more can be left if required.
     */
    public void clearFeedback(){
        feedbackJText.setText("");
    }
    /**
     * Register button handler for rate doctor button.
     * @param controller to respond to event.
     */
    public void addRateDoctorButtonHandler(ActionListener controller){
        rateJButton.addActionListener(controller);
    }
    /**
     * Display the appointment history for the patient - all those which have occurred
     * in the past.
     * @param appointments to be set.
     */    
    public void setAppointmentHistory(String[] appointments){
        if(appointments != null){
            appointmentHistoryJList.setListData(appointments);            
        }
    }
    /**
     * Display the prescription history for the patient - all those which have occurred
     * in the past 
     * @param prescriptions to be set.
     */
    public void setPrescriptionHistory(String[] prescriptions){
        if(prescriptions != null){
            prescriptionHistoryJList.setListData(prescriptions);
            System.out.println("Should've set prescription history");
        }
    }
    /**
     * Display the notes history for the patient - all those which have occurred
     * in the past
     * @param notes t be set.
     */
    public void setNotesHistory(String[] notes){
        if(notes != null){
            notesHistoryJList.setListData(notes);
        }
    }
    /** 
     * Add button handler to request appointment button.
     * @param controller to respond to event. 
     */
    public void addRequestAppointmentButtonHandler(ActionListener controller){
        requestAppointmentJButton.addActionListener(controller);
    }
    /**
     * Add button handler to logout button.
     * @param controller  to respond to event.
     */
    public void addLogoutButtonHandler(ActionListener controller){
        logoutJButton.addActionListener(controller);
    }
    
    // Variables declaration - do not modify                     
    private javax.swing.JButton accountTerminationJButton;
    private javax.swing.JLabel appointmentDOctorJLabel;
    private javax.swing.JLabel appointmentDateJLabel;
    private javax.swing.JSpinner appointmentDateJSpinner;
    private javax.swing.JComboBox<String> appointmentDoctorJCombo;
    private javax.swing.JLabel appointmentHistoryJLabel;
    private javax.swing.JList<String> appointmentHistoryJList;
    private javax.swing.JComboBox<String> appointmentTimeJCombo;
    private javax.swing.JLabel appointmentTimeJLabel;
    private javax.swing.JPanel appointmentsJPanel;
    private javax.swing.JTable appointmentsTable;
    private javax.swing.JTabbedPane backgroundJTabbedPane;
    private javax.swing.JList<String> currentDocRatingsJList;
    private javax.swing.JLabel currentPrescriptionJLabel;
    private javax.swing.JTextField currentPrescriptionJText;
    private javax.swing.JButton deleteMessageJButton;
    private javax.swing.JLabel detailsJLabel;
    private javax.swing.JPanel doctorJPanel;
    private javax.swing.JLabel doctorRatingJlabel;
    private javax.swing.JComboBox<String> doctorsDocIDJCombo;
    private javax.swing.JLabel doctorsJLabel;
    private javax.swing.JLabel feedbackJLabel;
    private javax.swing.JTextArea feedbackJText;
    private javax.swing.JPanel historyJPanel;
    private javax.swing.JPanel homeJPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JButton logoutJButton;
    private javax.swing.JLabel messagesJLabel;
    private javax.swing.JTable messagesTable;
    private javax.swing.JLabel notesHistoryJLabel;
    private javax.swing.JList<String> notesHistoryJList;
    private javax.swing.JLabel prescriptionHistoryJLabel;
    private javax.swing.JList<String> prescriptionHistoryJList;
    private javax.swing.JLabel rateDoctorJLabel;
    private javax.swing.JButton rateJButton;
    private javax.swing.JLabel ratingJLabel;
    private javax.swing.JSpinner ratingJSpinner;
    private javax.swing.JButton requestAppointmentJButton;
    private javax.swing.JLabel requestAppointmentJLabel;
    private javax.swing.JLabel titleJLabel;
    private javax.swing.JTextArea userInfoJText;
    private javax.swing.JLabel welcomeJLabel;
    private javax.swing.JLabel yourAppointmentsJLabel;
    // End of variables declaration                   
}