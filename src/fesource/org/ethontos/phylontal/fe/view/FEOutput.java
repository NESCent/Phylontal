/*
 * Phylontal FrontEnd - a GUI frontend for Phylontal
 * Copyright 2009-2011 Peter E. Midford
 * This file is part of Phylontal Frontend
 *
 * Phylontal Frontend is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Phylontal Frontend is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Phylontal Frontend.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Created on Oct 10, 2009
 * Last updated on April 27, 2011
 * 
 */
 
package org.ethontos.phylontal.fe.view;

import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class FEOutput extends JDialog {

    private Vector<String> lines;
    private int maxLine=0;
    private TextArea textScroll = null;
    
    static final long serialVersionUID = -5337816133478754753L;

    
    public FEOutput() throws HeadlessException {
        super();
        lines = new Vector<String>();
        // TODO Auto-generated constructor stub
    }

    // not sure about flushing contents of lines here
    public void resetText(){
        maxLine = 0;
        lines.clear();
    }

    public void showText() {
        Container cPane = this.getContentPane(); 
        if (textScroll == null) {
            textScroll = new TextArea(maxLine,lines.size());
            textScroll.enableInputMethods(false);
            cPane.add(textScroll);
        }
        textScroll.setText("");
        Iterator<String> it = lines.iterator();
        while (it.hasNext()){
            textScroll.append(it.next());
        }
        setVisible(true);
    }
    
    /**
     * Reads the software license file into the text area and exposes the dialog
     *
     */
    public void showLicense(){
        showResource("org/ethontos/phylontal/resources/COPYING","license");
    }


    /**
     * Reads the software license file into the text area and exposes the dialog
     *
     */
    public void showResource(String resourceName,String fileContents){
        URL u1 = ClassLoader.getSystemResource(resourceName);
        if (u1 == null){  //fail quietly here...
            JOptionPane.showMessageDialog(null, "ResName = " + resourceName);
            return;
        }
        resetText();
        BufferedReader lReader=null;
        Container cPane = this.getContentPane(); 
        if (textScroll == null) {
            textScroll = new TextArea(maxLine,lines.size());
            textScroll.enableInputMethods(false);
            cPane.add(textScroll);
        }
        textScroll.setText("");
        int lineCount=0;
        try {
            lReader = new BufferedReader(new InputStreamReader(u1.openStream()));
            while(lReader.ready()){
                textScroll.append(lReader.readLine()+"\n");
            }
            lReader.close();
        }
        catch (FileNotFoundException ex){
            System.out.println("Error opening " + fileContents + " file: " +  u1.getPath());
            try{
                if (lReader != null)
                    lReader.close();
            }
            catch (IOException e2){
                System.out.println("Second exception encountered closing " + fileContents + " file " + e2.toString());
            }
        }
        catch (IOException ex){
            System.out.println("Problem encountered reading the " + fileContents + " file at line " + lineCount);
            try{
                lReader.close();
            }
            catch (IOException e2){
                System.out.println("Second exception encountered closing " + fileContents + " file " + e2.toString());
            }
        }
        setSize(600,500);
        setVisible(true);
    }

}
