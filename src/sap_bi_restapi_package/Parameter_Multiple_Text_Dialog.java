

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package sap_bi_restapi_package;

import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.List;

 
public class Parameter_Multiple_Text_Dialog extends JDialog implements ActionListener {
	
    private static Parameter_Multiple_Text_Dialog dialog;
    private static ArrayList<String> values;
    private JList<String> list;
    
    private JTextField enter_text_field;
    private String[] mydata;
    private JScrollPane listScroller;
 
    /**
     * Set up and show the dialog.  The first Component argument
     * determines which frame the dialog depends on; it should be
     * a component in the dialog's controlling frame. The second
     * Component argument should be null if you want the dialog
     * to come up with its left corner in the center of the screen;
     * otherwise, it should be the component on top of which the
     * dialog should appear.
     */
    public static ArrayList<String> showDialog(Component frameComp, Component locationComp,
    		                                   String labelText, String title, String[] possibleValues) {
    	
    	values = null;
        Frame frame = JOptionPane.getFrameForComponent(frameComp);
        dialog = new Parameter_Multiple_Text_Dialog(frame, locationComp,
                                           labelText, title, possibleValues);
        dialog.setVisible(true);
        return values;
    }
    
    private void Initialize_List(String[] data) {
    	
    	mydata = data;
    	
    	if (data != null)
	    	list = new JList<String>(data) {
	            //Subclass JList to workaround bug 4832765, which can cause the
	            //scroll pane to not let the user easily scroll up to the beginning
	            //of the list.  An alternative would be to set the unitIncrement
	            //of the JScrollBar to a fixed value. You wouldn't get the nice
	            //aligned scrolling, but it should work.
	            public int getScrollableUnitIncrement(Rectangle visibleRect,
	                                                  int orientation,
	                                                  int direction) {
	                int row;
	                if (orientation == SwingConstants.VERTICAL &&
	                      direction < 0 && (row = getFirstVisibleIndex()) != -1) {
	                    Rectangle r = getCellBounds(row, row);
	                    if ((r.y == visibleRect.y) && (row != 0))  {
	                        Point loc = r.getLocation();
	                        loc.y--;
	                        int prevIndex = locationToIndex(loc);
	                        Rectangle prevR = getCellBounds(prevIndex, prevIndex);
	 
	                        if (prevR == null || prevR.y >= r.y) {
	                            return 0;
	                        }
	                        return prevR.height;
	                    }
	                }
	                return super.getScrollableUnitIncrement(visibleRect, orientation, direction);
	            }
	        };
	    else
	    	list = new JList<String>() {
	            //Subclass JList to workaround bug 4832765, which can cause the
	            //scroll pane to not let the user easily scroll up to the beginning
	            //of the list.  An alternative would be to set the unitIncrement
	            //of the JScrollBar to a fixed value. You wouldn't get the nice
	            //aligned scrolling, but it should work.
	            public int getScrollableUnitIncrement(Rectangle visibleRect,
	                                                  int orientation,
	                                                  int direction) {
	                int row;
	                if (orientation == SwingConstants.VERTICAL &&
	                      direction < 0 && (row = getFirstVisibleIndex()) != -1) {
	                    Rectangle r = getCellBounds(row, row);
	                    if ((r.y == visibleRect.y) && (row != 0))  {
	                        Point loc = r.getLocation();
	                        loc.y--;
	                        int prevIndex = locationToIndex(loc);
	                        Rectangle prevR = getCellBounds(prevIndex, prevIndex);
	 
	                        if (prevR == null || prevR.y >= r.y) {
	                            return 0;
	                        }
	                        return prevR.height;
	                    }
	                }
	                return super.getScrollableUnitIncrement(visibleRect, orientation, direction);
	            }
        	};
 
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        list.setLayoutOrientation(JList.VERTICAL);
		
		list.setVisibleRowCount(-1);
    	
    }
 
    
 
    private Parameter_Multiple_Text_Dialog(Frame frame, Component locationComp,
								  String labelText, String title, String[] data) {
        super(frame, title, true);
 
        //Create and initialize the buttons.
        JButton cancelButton = new JButton("CLEAR");
        cancelButton.setActionCommand("CLEAR");
        cancelButton.addActionListener(this);
        getRootPane().setDefaultButton(cancelButton);
        
        
        final JButton setButton = new JButton("SET");
        setButton.setActionCommand("SET");
        setButton.addActionListener(this);
        
        final JButton addButton = new JButton("ADD");
        addButton.setActionCommand("ADD");
        addButton.addActionListener(this);
        
 
        //main part of the dialog
        Initialize_List(data);
        
        listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(800, 800));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);
 
        //Create a container so that we can add a title around
        //the scroll pane.  Can't add a title directly to the
        //scroll pane because its background would be white.
        //Lay out the label and scroll pane from top to bottom.
        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        
        JLabel enter_text_label = new JLabel("To enter a new value below, enter the text and press ADD");
        listPane.add(enter_text_label);
        
        enter_text_field = new JTextField();
        listPane.add(enter_text_field);
        
        
        listPane.add(addButton);
        listPane.add(Box.createRigidArea(new Dimension(0,5)));
        
        JLabel label = new JLabel(labelText);
        label.setLabelFor(list);
        listPane.add(label);
        listPane.add(Box.createRigidArea(new Dimension(0,5)));
        
        listPane.add(listScroller);
        listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
 
        //Lay out the buttons from left to right.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(cancelButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(setButton);
 
        //Put everything together, using the content pane's BorderLayout.
        Container contentPane = getContentPane();
        contentPane.add(listPane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
        
        pack();
        setLocationRelativeTo(locationComp);
    }
 
    //Handle clicks on the Set and Cancel buttons.
    public void actionPerformed(ActionEvent e) {
        if ("SET".equals(e.getActionCommand())) {
        	List<String> templist = list.getSelectedValuesList();
        	if (templist.isEmpty())
        		Parameter_Multiple_Text_Dialog.values = null;
        	else
        		Parameter_Multiple_Text_Dialog.values = (ArrayList<String>)templist;
        	Parameter_Multiple_Text_Dialog.dialog.setVisible(false);
        }
        else if ("CLEAR".equals(e.getActionCommand())) {
        	Parameter_Multiple_Text_Dialog.values = null;
        	Parameter_Multiple_Text_Dialog.dialog.setVisible(false);
        }
        else if ("ADD".equals(e.getActionCommand())) {
        	ArrayList<String> templist = new ArrayList<String>();
        	if (mydata != null) {
        		for (int ii=0; ii < mydata.length; ii++)
        			templist.add(mydata[ii]);
        	}
        	
        	templist.add(enter_text_field.getText());
        	
        	String[] tempdata = templist.toArray(new String[templist.size()]);
        	
        	listScroller.remove(list);
        	this.Initialize_List(tempdata);
        	listScroller.setViewportView(list);
        	
        	this.revalidate();
        	this.repaint();
        }
    }
}//class