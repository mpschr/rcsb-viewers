package org.rcsb.mbt.glscene.jogl;

//  $Id: ChainGeometry.java,v 1.1 2007/02/08 02:38:52 jbeaver Exp $
//
//  Copyright 2000-2004 The Regents of the University of California.
//  All Rights Reserved.
//
//  Permission to use, copy, modify and distribute any part of this
//  Molecular Biology Toolkit (MBT)
//  for educational, research and non-profit purposes, without fee, and without
//  a written agreement is hereby granted, provided that the above copyright
//  notice, this paragraph and the following three paragraphs appear in all
//  copies.
//
//  Those desiring to incorporate this MBT into commercial products
//  or use for commercial purposes should contact the Technology Transfer &
//  Intellectual Property Services, University of California, San Diego, 9500
//  Gilman Drive, Mail Code 0910, La Jolla, CA 92093-0910, Ph: (858) 534-5815,
//  FAX: (858) 534-7345, E-MAIL:invent@ucsd.edu.
//
//  IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
//  DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING
//  LOST PROFITS, ARISING OUT OF THE USE OF THIS MBT, EVEN IF THE
//  UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.//
//  THE MBT PROVIDED HEREIN IS ON AN "AS IS" BASIS, AND THE
//  UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT,
//  UPDATES, ENHANCEMENTS, OR MODIFICATIONS. THE UNIVERSITY OF CALIFORNIA MAKES
//  NO REPRESENTATIONS AND EXTENDS NO WARRANTIES OF ANY KIND, EITHER IMPLIED OR
//  EXPRESS, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
//  MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE, OR THAT THE USE OF THE
//  MBT WILL NOT INFRINGE ANY PATENT, TRADEMARK OR OTHER RIGHTS.
//
//  For further information, please see:  http://mbt.sdsc.edu
//
//  History:
//  $Log: ChainGeometry.java,v $
//  Revision 1.1  2007/02/08 02:38:52  jbeaver
//  version 1.50
//
//  Revision 1.1  2006/09/20 16:50:42  jbeaver
//  first commit - branched from ProteinWorkshop
//
//  Revision 1.2  2006/09/02 18:52:28  jbeaver
//  *** empty log message ***
//
//  Revision 1.1  2006/08/24 17:39:03  jbeaver
//  *** empty log message ***
//
//  Revision 1.1  2006/03/09 00:18:55  jbeaver
//  Initial commit
//
//  Revision 1.0 2005/04/04 00:12:54  moreland
//


// package org.rcsb.mbt.viewers.GlStructureViewer;


import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.rcsb.mbt.model.*;
import org.rcsb.mbt.model.attributes.*;
import org.rcsb.mbt.ui.views.StructureViewerImpl.CrossSectionStyle;
import org.rcsb.mbt.ui.views.StructureViewerImpl.HelixGeometry;
import org.rcsb.mbt.ui.views.StructureViewerImpl.SsGeometry;



import com.sun.opengl.util.GLUT;



/**
 * ChainGeometry.java<BR>
 *
 * Please complete these missing tags
 * @author	John L. Moreland
 * @copyright	UCSD
 * @see
 */
public class ChainGeometry
	extends DisplayListGeometry
{
	private final Interpolator interpolator =
		new Interpolator( Interpolator.HERMITE );
	
	/**
	 * TODO: These should be enums.
	 */
	public static final int RIBBON_SIMPLE_LINE = 0;
	public static final String RIBBON_SIMPLE_LINE_DESCRIPTION = "Simple Line";
	public static final int RIBBON_TRADITIONAL = 1;
	public static final String RIBBON_TRADITIONAL_DESCRIPTION = "Traditional";
	public static final int RIBBON_CYLINDRICAL_HELICES = 2;
	public static final String RIBBON_CYLINDRICAL_HELICES_DESCRIPTION = "Traditional / Cylinders";
	
	private int ribbonForm = RIBBON_TRADITIONAL;
	private boolean ribbonsAreSmoothed = true;
					// these were set from defaults specified in the mutators.  Problem is,
					// that requires bringing the mutator suite into this project, which
					// we're trying to avoid, at the moment.
					//
					// they would be better set from the constructor.  I've provided a
					// 'setter' function for each, for the interim.
					//
					// these are the values that the mutator defaults to.
					//
					// 07-May-08  rickb
					//
	/**
	 *  Construct a new ChainGeometry object.
	 */
	public ChainGeometry( )
	{
	}


	/**
	 * Please complete the missing tags for main
	 * @param
	 * @return
	 * @throws
	 */
	public final int getDisplayList( final int displayList, final StructureComponent structureComponent, final Style style, final GL gl, final GLU glu, final GLUT glut)
	{
		return this.getDisplayList( displayList, structureComponent, style, gl, glu, glut);
	}

	// This does not apply to ChainGeometry. Use setHelixForm, etc. instead.
	
	public void setForm(final int form) {

	}	
	
	/**
	 * Provide setter for this, right now.  May change to put in the constructor...
	 *
	 * @param eForm - one of the values for the ribbon form.  These should be enums,
	 * 				  but they're currently defined as ints (boo.)
	 * @return
	 */

	public DisplayLists[] getDisplayLists(final StructureComponent structureComponent, final Style style, final GL gl, final GLU glu, final GLUT glut) {
		//
		// Handle quality, form, and shared display lists.
		//

		DisplayLists[] lists = null;
		
		final float quality = this.getQuality( );
		final int form = this.getForm( );

		final Chain chain = (Chain)structureComponent;
		
		//
		// Generate the display List
		//

		final Structure structure = chain.getStructure( );
		final StructureMap structureMap = structure.getStructureMap( );
		final StructureStyles structureStyles = structureMap.getStructureStyles( );

		// Create or over-write this chain geometry's display list.
//		if ( displayList < 0 )
//			displayList = gl.glGenLists( 1 );
//		else
//			gl.glDeleteLists( displayList, 1 );
//		gl.glNewList( displayList, GL.GL_COMPILE );

//		gl.glPushMatrix( );

		try {
			boolean ribbon = false;
	    	
	    	final float helixQuality = 0.8f;
	        final float coilQuality = 0.8f;
	        final float turnQuality = 0.8f;
	        final float strandQuality = 0.8f;

	        int helixSmoothingSteps = -1;
	        int strandSmoothingSteps = -1;
	        int turnSmoothingSteps = -1;
	        int coilSmoothingSteps = -1;
	        
	        int helixCsType = -1;
	        //int helixCsType = CrossSectionStyle.RECTANGULAR_RIBBON;
	        int strandCsType = -1;
	        int turnCsType = -1;
	        int coilCsType = -1;
	        int helixSsShape = -1;
			
	        if(this.ribbonsAreSmoothed) {
	        	coilSmoothingSteps = 2;
	        	turnSmoothingSteps = 2;
	        	helixSmoothingSteps = 0;
	        	strandSmoothingSteps = 2;
	        } else {
	        	coilSmoothingSteps = 0;
	        	turnSmoothingSteps = 0;
	        	helixSmoothingSteps = 0;
	        	strandSmoothingSteps = 0;
	        }
	        
			switch(this.ribbonForm) {
        	case RIBBON_CYLINDRICAL_HELICES:
                coilCsType = CrossSectionStyle.ROUNDED_TUBE;
                turnCsType = CrossSectionStyle.ROUNDED_TUBE;
                helixCsType = CrossSectionStyle.REGULAR_POLYGON;
                helixSsShape = HelixGeometry.CYLINDER;
            	strandCsType = CrossSectionStyle.RECTANGULAR_RIBBON;
                
                ribbon = true;
        		break;
        	case RIBBON_TRADITIONAL:
        		coilCsType = CrossSectionStyle.ROUNDED_TUBE;
                turnCsType = CrossSectionStyle.ROUNDED_TUBE;
                helixCsType = CrossSectionStyle.REGULAR_POLYGON;
                helixSsShape = HelixGeometry.RIBBON;
                strandCsType = CrossSectionStyle.RECTANGULAR_RIBBON;
            	
                ribbon = true;
        		break;
        	case RIBBON_SIMPLE_LINE:
        		coilCsType = CrossSectionStyle.ROUNDED_TUBE;
                turnCsType = CrossSectionStyle.ROUNDED_TUBE;
                helixCsType = CrossSectionStyle.REGULAR_POLYGON;
                helixSsShape = HelixGeometry.RIBBON;
            	strandCsType = CrossSectionStyle.RECTANGULAR_RIBBON;
            	
            	ribbon = false;
        		break;
        	default:
        		(new Exception()).printStackTrace();
        	}
			
			lists = SsGeometry.createSs(chain, structureMap, structureStyles, ribbon, 
					helixQuality, strandQuality, turnQuality, coilQuality,
					helixSmoothingSteps, strandSmoothingSteps, turnSmoothingSteps,
					coilSmoothingSteps, helixCsType, strandCsType, turnCsType,
					coilCsType, helixSsShape, gl, glu, glut);
		} catch(final Exception e) {
			e.printStackTrace();
		}
		return lists;
	}


	public int getRibbonForm() {
		return this.ribbonForm;
	}


	public void setRibbonForm(final int ribbonForm) {
		this.ribbonForm = ribbonForm;
	}


	public boolean isRibbonsAreSmoothed() {
		return this.ribbonsAreSmoothed;
	}


	public void setRibbonsAreSmoothed(final boolean ribbonsAreSmoothed) {
		this.ribbonsAreSmoothed = ribbonsAreSmoothed;
	}
}
