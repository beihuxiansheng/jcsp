
//////////////////////////////////////////////////////////////////////
//                                                                  //
//  JCSP ("CSP for Java") Libraries                                 //
//  Copyright (C) 1996-2018 Peter Welch, Paul Austin and Neil Brown //
//                2001-2004 Quickstone Technologies Limited         //
//                2005-2018 Kevin Chalmers                          //
//                                                                  //
//  You may use this work under the terms of either                 //
//  1. The Apache License, Version 2.0                              //
//  2. or (at your option), the GNU Lesser General Public License,  //
//       version 2.1 or greater.                                    //
//                                                                  //
//  Full licence texts are included in the LICENCE file with        //
//  this library.                                                   //
//                                                                  //
//  Author contacts: P.H.Welch@kent.ac.uk K.Chalmers@napier.ac.uk   //
//                                                                  //
//////////////////////////////////////////////////////////////////////

package jcsp.plugNplay.ints;

import jcsp.lang.*;

/**
 * Bitwise <I>nands</I> two integer streams to one stream.
 *
 * <H2>Process Diagram</H2>
 * <!-- INCORRECT DIAGRAM: <p><img src="doc-files/NandInt1.gif"></p> -->
 * <PRE>
 *    in0  _________
 *   -->--|         | out
 *    in1 | NandInt |-->--
 *   -->--|_________|
 * </PRE>
 * <H2>Description</H2>
 * <TT>NandInt</TT> is a process whose output strean is the bitwise <I>nand</I>
 * of the integers on its input streams.
 * <P>
 * <H2>Channel Protocols</H2>
 * <TABLE BORDER="2">
 *   <TR>
 *     <TH COLSPAN="3">Input Channels</TH>
 *   </TR>
 *   <TR>
 *     <TH>in0, in1</TH>
 *     <TD>int</TD>
 *       All channels in this package carry integers.
 *     </TD>
 *   </TR>
 *   <TR>
 *     <TH COLSPAN="3">Output Channels</TH>
 *   </TR>
 *   <TR>
 *     <TH>out</TH>
 *     <TD>int</TD>
 *     <TD>
 *       All channels in this package carry integers.
 *     </TD>
 *   </TR>
 * </TABLE>
 * <P>
 * <H2>Example</H2>
 * The following example shows how the use of this process in a small program.
 * The program also uses some of the other building block processes.
 * It generates a sequence of numbers, rounds each odd number down to
 * the nearest even number, negates them and prints them to the screen.
 *
 * <PRE>
 * import jcsp.lang.*;
 * import jcsp.plugNplay.ints.*;
 * 
 * public class NandIntExample {
 * 
 *   public static void main (String[] argv) {
 * 
 *     final One2OneChannelInt a = Channel.one2oneInt ();
 *     final One2OneChannelInt b = Channel.one2oneInt ();
 *     final One2OneChannelInt c = Channel.one2oneInt ();
 *     final One2OneChannelInt d = Channel.one2oneInt ();
 * 
 *     new Parallel (
 *       new CSProcess[] {
 *         new NumbersInt (a.out ()),
 *         new GenerateInt (b.out (), Integer.MAX_VALUE - 1),
 *         new NandInt (a.in (), b.in (), c.out ()),
 *         new SuccessorInt (c.in (), d.out ()),
 *         new PrinterInt (d.in (), "--> ", "\n")
 *       }
 *     ).run ();
 * 
 *   }
 * 
 * }
 * </PRE>
 *
 * @author P.H. Welch and P.D. Austin
 */
public final class NandInt implements CSProcess
{
   /** The first input Channel */
   private final ChannelInputInt in0;
   
   /** The second input Channel */
   private final ChannelInputInt in1;
   
   /** The output Channel */
   private final ChannelOutputInt out;
   
   /**
    * Construct a new NandInt process with the input Channels in0 and in1 and the
    * output Channel out. The ordering of the Channels in0 and in1 make
    * no difference to the functionality of this process.
    *
    * @param in0 the first input Channel
    * @param in1 the second input Channel
    * @param out the output Channel
    */
   public NandInt(final ChannelInputInt in0, final ChannelInputInt in1, final ChannelOutputInt out)
   {
      this.in0 = in0;
      this.in1 = in1;
      this.out = out;
   }
   
   /**
    * The main body of this process.
    */
   public void run()
   {
      final ProcessReadInt[] procs = {new ProcessReadInt(in0), new ProcessReadInt(in1)};
      final Parallel par = new Parallel(procs);
      
      while (true)
      {
         par.run();
         final int i0 = procs[0].value;
         final int i1 = procs[1].value;
         out.write(~ (i0 & i1));
      }
   }
}
