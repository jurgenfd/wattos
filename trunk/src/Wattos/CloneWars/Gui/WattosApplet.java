/*
 * WattosApplet.java
 *
 * Created on March 29, 2006, 9:58 AM
 */

package Wattos.CloneWars.Gui;

import Wattos.CloneWars.*;
import Wattos.Episode_II.*;
import Wattos.Utils.General;
import Wattos.Utils.InOut;
import java.applet.Applet;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;

/**
 *
 * @author  jurgen
 */
public class WattosApplet extends Applet implements ActionListener {
    private static final long serialVersionUID = -3124352860022240944L;
    final String DEFAULT_MODULE_NAME = "UserInterface";
    String moduleName = DEFAULT_MODULE_NAME;
    JFrame moduleFrame = null;
    /**
     * Initializes the applet WattosApplet
     */
    public void init() {
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    initComponents();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }                
        moduleName = getParameter("MODULE");
        if ( moduleName == null ) {
            moduleName = "Unknown Module";
            String msg = "<APPLET> tag MODULE not found.\nTry using the Web Start method instead.";
            jTextArea1.setText(msg);
            General.showError(msg);
        } else {
            setName(moduleName);        
        }
    }

    /** Thread is called by Event Dispatch Thread (EDT)
     */
    public void start() {
        setVisible(true);
        jButton1ActionPerformed(null); // starts a module.
    }
    
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();

        setFont(new java.awt.Font("SansSerif", 0, 12));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Wattos"));
        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setFont(new java.awt.Font("SansSerif", 0, 12));
        jTextArea1.setRows(3);
        jTextArea1.setText("The Wattos module will open in a separate window.\n\nPlease do not close this window!");

        jButton1.setText("Restart Module");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(12, 12, 12)
                .add(jTextArea1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE))
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jButton1)
                .addContainerGap(248, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jTextArea1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 24, Short.MAX_VALUE)
                .add(jButton1)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        jTextArea1.setEnabled(true);
        jButton1.setEnabled(false);
        
        UserInterface ui = null;
        
        if ( moduleName.equalsIgnoreCase("Sjors") ) {
            moduleFrame = new Sjors(this);
        } else if ( moduleName.equalsIgnoreCase("UserInterface") ) {
            boolean ignoreOldDumpIfPresent = true;
            ui = UserInterface.init(ignoreOldDumpIfPresent);
            moduleFrame = new WattosGui(ui,this); // this applet is the action listener.            
        } else {
            String msg = "Undefined module requested: " + moduleName;
            jTextArea1.setText(msg);
            General.showError(msg);
            return;
        }        
        if ( moduleFrame == null ) {
            General.showError("Unable to get module frame initialized.");
            return;
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                moduleFrame.setVisible(true);
            }
        });
        // After it is visible and responsive let it run the script in the same queue
        // on a different thread.
        if ( moduleFrame instanceof WattosGui ) {
            String scriptName = getParameter("WATTOSSCRIPT");
            if ( scriptName != null ) {
                URL url = InOut.getUrlFileFromName(scriptName);
                if ( url == null ) {
                    General.showError("Unable to translate script to url");                    
                } else {
                    Object[] methodArgs = { url };
                    ui.commandHub.ExecuteMacroUser(methodArgs);
                }
            }         
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * Listens to the moduleName closed action
     */
    public void actionPerformed(ActionEvent e) {
        jTextArea1.setEnabled(false);
        jButton1.setEnabled(true);        
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
    
}
