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
 * Created on Nov 21, 2009
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.fe.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JViewport;

import org.apache.log4j.Logger;
import org.ethontos.phylontal.project.phylo.ExternalNode;
import org.ethontos.phylontal.project.phylo.GraphTree;
import org.ethontos.phylontal.project.phylo.PhyloNode;

public class SimpleTreePane extends NamePanel {
    
    private static final String[] filters = {"None"};    // fill me in
    
    private int treeWidth = 200;   //this should be somewhat mutable
    private int tipHeight = 20;  //as should this
    private int rootOffset = 10;
    
    private final int nodeCircleSize = 6;
    private final int nodeCircleOffset = nodeCircleSize/2;
    
    private GraphTree tree = null;
    
    private int maxHeight = 1;  // default to something that won't blow up
    private int unitBranchLength;
    
    private JPanel tipsPane;
    
    private boolean traceValid = false;
    
    private HashMap<PhyloNode,JLabel> nodeLabels = null;
    private HashMap<JLabel,PhyloNode> labelNodes = null;
    private HashMap<JLabel,Integer> labelOffsets = null;
    private HashMap<PhyloNode,Point> nodePoints = null;
    private HashMap<PhyloNode,Integer> nodeHeights = null;
    
    private SelectionObservable selectedState;
    
    private PhyloNode mySelected;
    
    static Logger logger = Logger.getLogger(SimpleTreePane.class.getName());

    
    public SimpleTreePane(){
        //final SimpleMouseListener myMouseListener = new SimpleMouseListener();
        //addMouseListener(myMouseListener);
        //addMouseMotionListener(myMouseListener);
        this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        tipsPane = new JPanel();
        tipsPane.setLayout(new BoxLayout(tipsPane, BoxLayout.Y_AXIS));
        tipsPane.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        add(Box.createRigidArea(new Dimension(treeWidth,2)));
        add(tipsPane);
        selectedState = new SelectionObservable(this);

    }

    public void setViewPort(JViewport viewport) {
        // TODO Auto-generated method stub
        
    }

    
    @Override
    public boolean isSelected() {
        return mySelected != null;
    }
    
    @Override
    public Object getSelected() {
        if (isSelected())
            return mySelected;
        else
            return null;
    }
    
    @Override
    public void clearSelected(){
        if (mySelected != null){
            JLabel oldSelected = lookupLabel(mySelected);
            oldSelected.setForeground(Color.BLACK);
        }
        mySelected = null;
    }

    public void setTree(GraphTree selectedTree) {
        tree = selectedTree;
        if (nodeLabels == null){
            final int size = tree.getExternalNodes().size();
            nodeLabels = new HashMap<PhyloNode,JLabel>(size);
            labelNodes = new HashMap<JLabel,PhyloNode>(size);
            labelOffsets = new HashMap<JLabel,Integer>(size);
            nodePoints = new HashMap<PhyloNode,Point>(size*size/2);
            nodeHeights = new HashMap<PhyloNode,Integer>(size*size/2);
        }
        setHeights(tree,tree.getRoot(),0);
        maxHeight = getMaxHeight(nodeHeights);
        unitBranchLength = (int)(0.75*treeWidth/maxHeight);
        addTips(tipsPane,tree,tree.getRoot());
    }
    
    private void addTips(JPanel tipsP, GraphTree tree, PhyloNode node){
        if (node instanceof ExternalNode){
            final String label = ((ExternalNode)node).getTaxonLabel();
            final JLabel tipLabel = new JLabel(label);
            tipLabel.addComponentListener(new SimpleComponentListener(tipsP));
            tipLabel.addMouseListener(new SimpleMouseListener(this));
            tipsP.add(tipLabel);
            tipsP.add(Box.createRigidArea(new Dimension(10,10)));
            nodeLabels.put(node,tipLabel);
            labelNodes.put(tipLabel, node);
            tipsP.add(Box.createRigidArea(new Dimension(10,10)));
            logger.debug("Added tipLabel " + tipLabel + " for node " + node);
        }
        else {
            List<PhyloNode> children = tree.getChildren(node);
            for (PhyloNode kid : children){
                addTips(tipsP,tree,kid);
            }
        }
    }
    
    public void traceTree(){
        // first make sure all the tips are known
        boolean tipsValid = true;
        nodePoints.clear();
        if (nodeLabels != null){
            Set <Entry<PhyloNode,JLabel>> eset = nodeLabels.entrySet();
            for(Entry<PhyloNode,JLabel> e : eset){
                JLabel l = e.getValue();
                if (l != null){
                    Integer yI = labelOffsets.get(l);
                    if (yI != null){
                        nodePoints.put(e.getKey(), new Point(unitBranchLength*nodeHeights.get(e.getKey())+rootOffset,yI.intValue()+tipHeight/2));
                    }
                    else{
                        tipsValid = false;
                        logger.error("yI is missing; tipsValid is false at " + l.getText());
                    }
                }
                else{
                    tipsValid = false;
                    logger.debug("label is missing; tipsValid is false at ");
                }
            }
        }
        else {
            tipsValid = false;
        }
        // then plot points for the tree
        if (tipsValid){
            traceDown(tree,tree.getRoot());
            traceValid = true;
       }
        else
            logger.debug("tipsValid is false");
    }
    
    public void paint(Graphics g){
        paintComponent(g);
        paintBorder(g);
        paintChildren(g);
        if (traceValid)
            drawDown(g,tree,tree.getRoot());
    }
    
    private void setHeights(GraphTree tree2, PhyloNode node, int depth){
        nodeHeights.put(node, depth);
        if (!tree2.isExternal(node)){
            List<PhyloNode> kids = tree2.getChildren(node);
            for(PhyloNode kid : kids){
                setHeights(tree2,kid,depth+1);
            }
        }
    }
    
    private int getMaxHeight(HashMap<PhyloNode, Integer> nodeHeights2){
        int max = 0;
        for (Integer height : nodeHeights2.values()){
            if (height.intValue() > max)
                max = height.intValue();
        }
        return max;
    }
    
    private void traceDown(GraphTree tree2, PhyloNode phyloNode){
        if (tree2.isExternal(phyloNode)){
            traceHome(tree2,phyloNode);
        }
        else {
            List<PhyloNode> children = tree2.getChildren(phyloNode);
            for (PhyloNode kid : children){
                traceDown(tree2,kid);
            }
        }
    }
    
    private void traceHome(GraphTree tree2, PhyloNode node){
        if (!node.equals(tree2.getRoot())){
            final PhyloNode parent = tree2.getParent(node);
            if (!nodePoints.containsKey(parent)){
                final Point myPoint = nodePoints.get(node);
                Point parentPoint = new Point(nodeHeights.get(parent)*unitBranchLength+rootOffset,myPoint.y);
                nodePoints.put(parent,parentPoint);
            }
            else {
               Point parentPoint = nodePoints.get(parent);
               int oldy = parentPoint.y;
               parentPoint.setLocation(parentPoint.x,(oldy+nodePoints.get(node).y)/2);
            }
            traceHome(tree2,parent);
        }
    }

    private void drawDown(Graphics g, GraphTree tree2, PhyloNode kid2){ 
        final Point myPoint = nodePoints.get(kid2);
        if (!kid2.equals(tree2.getRoot())){
            final Point parentPoint = nodePoints.get(tree2.getParent(kid2));
            g.drawLine(myPoint.x, myPoint.y, parentPoint.x, myPoint.y);
            g.drawLine(parentPoint.x,myPoint.y,parentPoint.x,parentPoint.y);
            g.drawOval(parentPoint.x-nodeCircleOffset, parentPoint.y-nodeCircleOffset, nodeCircleSize, nodeCircleSize);
        }
        else {  // draw a little 'tail' for the root
            g.drawLine(myPoint.x, myPoint.y,0,myPoint.y);
        }
        if (!tree2.isExternal(kid2)){
            List<PhyloNode> children = tree2.getChildren(kid2);
            for (PhyloNode kid : children){
                drawDown(g, tree2,kid);
            }
        }
    }


    @Override
    public String[] getFilters() {
        return filters;
    }
    
    public PhyloNode lookupNode(JLabel source) {
        return labelNodes.get(source);
    }

    public JLabel lookupLabel(PhyloNode mySelected2) {
        return nodeLabels.get(mySelected2);
    }


    
    class SimpleComponentListener implements ComponentListener{

        private JPanel owner;
        final private Point holdingPoint = new Point();
        final private Point parentLoc = new Point();
        SimpleComponentListener (JPanel p_owner){
            owner = p_owner;
        }
        
        @Override
        public void componentHidden(ComponentEvent e) {
            logger.debug("Hit component hidden");
            // TODO Auto-generated method stub
            
        }

        @Override
        public void componentMoved(ComponentEvent e) {
             Component lc = e.getComponent();
             if (lc instanceof JLabel){
                 if (lc.getParent() == owner){
                     lc.getLocation(holdingPoint);
                     owner.getLocation(parentLoc);
                     labelOffsets.put((JLabel)lc,parentLoc.y+holdingPoint.y);
                     if (owner.getParent() instanceof SimpleTreePane){
                         ((SimpleTreePane)owner.getParent()).traceTree();
                     }
                     else
                         logger.error("Something's funny: " + owner.getParent());
                 }
             }
            // TODO Auto-generated method stub
            
        }

        @Override
        public void componentResized(ComponentEvent e) {
            Component lc = e.getComponent();
            if (lc instanceof JLabel){
                if (lc.getParent() == owner){
                    lc.getLocation(holdingPoint);
                    owner.getLocation(parentLoc);
                    logger.debug("Component resized: " + holdingPoint.x + "; " + holdingPoint.y);
                    labelOffsets.put((JLabel)lc,parentLoc.y+holdingPoint.y);
                    if (owner.getParent() instanceof SimpleTreePane){
                        ((SimpleTreePane)owner.getParent()).traceTree();
                    }
                    else
                        logger.error("Something's funny: " + owner.getParent());
                }
            }
            
        }

        @Override
        public void componentShown(ComponentEvent e) {
            logger.debug("Hit component shown");
            // TODO Auto-generated method stub            
        }
        


    }

    static class SimpleMouseListener implements MouseListener{
        
        SimpleTreePane owner;
        
        public SimpleMouseListener(SimpleTreePane o) {
            owner = o;
        }


        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("mouse clicked " + e.getSource());
            Object source = e.getSource();
            if (source instanceof JLabel){
                PhyloNode sourceNode = owner.lookupNode((JLabel)source);
                if (!sourceNode.equals(owner.mySelected)){
                    if (owner.mySelected != null){
                        JLabel oldSelected = owner.lookupLabel(owner.mySelected);
                        oldSelected.setForeground(Color.BLACK);
                    }
                    owner.mySelected = sourceNode;
                    ((JLabel)source).setForeground(Color.WHITE);
                    owner.selectedState.setSelected(sourceNode);
                }
            }
            
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //System.out.println("mouse entered " + e.getSource());
            // TODO Auto-generated method stub
            
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //System.out.println("mouse exited " + e.getSource());
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

    @Override
    public void addSelectionObserver(Observer o) {
        selectedState.addObserver(o);
    }
    

    static class SelectionObservable extends Observable{
        
        SimpleTreePane owner;
        
        SelectionObservable(SimpleTreePane o){
            owner = o;
        }
        
        void setSelected(Object o){
            setChanged();
            notifyObservers(o);
        }
                
    }
}
