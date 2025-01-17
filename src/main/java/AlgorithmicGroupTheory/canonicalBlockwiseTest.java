package AlgorithmicGroupTheory;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.IntStream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.openscience.cdk.Atom;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.group.Permutation;
import org.openscience.cdk.group.PermutationGroup;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IBond.Order;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

public class canonicalBlockwiseTest extends groupFunctions {
    public static int size = 0;
    public static int hIndex = 0;
    public static int count = 0;
    public static boolean verbose = false;
    public static SDFWriter outFile;
    public static String formula;
    public static String filedir;
    public static ArrayList<int[][]> output = new ArrayList<int[][]>();
    public static ArrayList<String> inchis = new ArrayList<String>();
    public static int[] degrees;
    public static int[] initialDegrees;
    public static int[] partition;
    public static ArrayList<Integer> initialPartition;
    public static ArrayList<Integer> inputPartition;
    public static IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
    public static IAtomContainer atomContainer = builder.newInstance(IAtomContainer.class);
    public static List<ArrayList<Integer>> partitionList = new ArrayList<ArrayList<Integer>>();
    public static ArrayList<ArrayList<Permutation>> representatives =
            new ArrayList<ArrayList<Permutation>>();
    public static List<String> symbols = new ArrayList<String>();
    public static ArrayList<Integer> occurrences = new ArrayList<Integer>();
    public static Map<String, Integer> valences;
    public static PrintWriter pWriter;
    public static int[][] max;
    public static int[][] L;
    public static int[][] C;

    static {
        // The atom valences from CDK.
        valences = new HashMap<String, Integer>();

        valences.put("C", 4);
        valences.put("N", 5);
        valences.put("O", 2);
        valences.put("S", 6);
        valences.put("P", 5);
        valences.put("F", 1);
        valences.put("I", 1);
        valences.put("Cl", 1);
        valences.put("Br", 1);
        valences.put("H", 1);
    }

    /**
     * InChI Generator from CDK.
     *
     * @param molecule IAtomContainer
     * @return
     * @throws CDKException
     */
    public static String inchiGeneration(IAtomContainer molecule) throws CDKException {
        String inchi = InChIGeneratorFactory.getInstance().getInChIGenerator(molecule).getInchi();
        return inchi;
    }

    /**
     * The main function for the block based canonical matrix generation.
     *
     * @param degrees ArrayList<Integer> atom valences
     * @param partition ArrayList<Integer> atom partitioning
     * @param filedir String The directory to store the output sdf file
     * @throws IOException
     * @throws CloneNotSupportedException
     * @throws CDKException
     */

    /**
     * deneme public static void canonicalBlockbasedGenerator(ArrayList<Integer> degrees,
     * ArrayList<Integer> partition, String filedir) throws IOException, CloneNotSupportedException,
     * CDKException{ long startTime = System.nanoTime(); initialDegrees=degrees;
     * size=degrees.size(); List<ArrayList<Integer>> newDegrees= distributeHydrogens(partition,
     * degrees); partitionList.add(0,inputPartition); for(ArrayList<Integer> degree: newDegrees) {
     * gen(degree); }
     *
     * <p>long endTime = System.nanoTime()- startTime; double seconds = (double) endTime /
     * 1000000000.0; DecimalFormat d = new DecimalFormat(".###"); System.out.println("time"+"
     * "+d.format(seconds)); }*
     */

    /**
     * Distribute the hydrogens in all possible ways to other atoms.
     *
     * @param partition ArrayList<Integer> atom partitioning
     * @param degrees ArrayList<Integer> atom valences
     * @return
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws CloneNotSupportedException
     * @throws CDKException
     */

    /**
     * deneme public static List<ArrayList<Integer>> distributeHydrogens(ArrayList<Integer>
     * partition, ArrayList<Integer> degrees) throws FileNotFoundException,
     * UnsupportedEncodingException, CloneNotSupportedException, CDKException{
     * List<ArrayList<Integer>> degreeList= new ArrayList<ArrayList<Integer>>(); List<int[]>
     * distributions= HydrogenDistributor.run(partition,degrees);
     * inputPartition=redefineThePartition(partition); hIndex= distributions.get(0).length;
     * for(int[] dist: distributions) { ArrayList<Integer> newDegree= new ArrayList<Integer>();
     * for(int i=0;i<dist.length;i++) { newDegree.add(i,(degrees.get(i)-dist[i])); }
     * degreeList.add(newDegree); } return degreeList; }*
     */

    /**
     * deneme public static List<int[]> distributeHydrogens(int[] partition, int[] degrees) throws
     * FileNotFoundException, UnsupportedEncodingException, CloneNotSupportedException,
     * CDKException{ List<int[]> degreeList= new ArrayList<int[]>(); List<int[]> distributions=
     * HydrogenDistributor.run(partition,degrees); hIndex= distributions.get(0).length;
     * initialPartition=partition; for(int[] dist: distributions) { int[] newDegree= new
     * int[hIndex]; for(int i=0;i<dist.length;i++) { newDegree[i]=(degrees[i]-dist[i]); }
     * degreeList.add(newDegree); } return degreeList; }*
     */
    /**
     * Once the hydrogens are distributed, we need to redefine the input partition.
     *
     * @param partition ArrayList<Integer> atom partitioning
     * @return
     */
    public static ArrayList<Integer> redefineThePartition(ArrayList<Integer> partition) {
        ArrayList<Integer> newPart = new ArrayList<Integer>();
        for (int i = 0; i < partition.size() - 1; i++) {
            newPart.add(partition.get(i));
        }
        return newPart;
    }

    /**
     * Theorem 3.2.1
     *
     * @param degrees degree sequence
     */
    public static void maximalMatrix() {
        max = new int[hIndex][hIndex];
        for (int i = 0; i < hIndex; i++) {
            for (int j = 0; j < hIndex; j++) {
                int di = degrees[i];
                int dj = degrees[j];

                if (i == j) {
                    max[i][j] = 0;
                } else {
                    if (di != dj) {
                        max[i][j] = Math.min(di, dj);
                    } else if (di == dj && i != j) {
                        // }else {
                        max[i][j] = (di - 1);
                    }
                }
            }
        }
    }

    /**
     * L; upper triangular matrix like given in 3.2.1. For (i,j), after the index, giving the
     * maximum line capacity.
     *
     * @param degrees
     * @return upper triangular matrix
     */
    public static void upperTriangularL() {
        L = new int[hIndex][hIndex];
        for (int i = 0; i < hIndex; i++) {
            for (int j = i + 1; j < hIndex; j++) {
                L[i][j] = Math.min(degrees[i], Lsum(i, j + 1));
            }
        }
    }

    public static int Lsum(int i, int j) {
        int sum = 0;
        for (int k = j; k < hIndex; k++) {
            sum = sum + max[i][k];
        }
        return sum;
    }

    public static int Csum(int i, int j) {
        int sum = 0;
        for (int k = i; k < hIndex; k++) {
            sum = sum + max[k][j];
        }
        return sum;
    }

    /**
     * C; upper triangular matrix like given in 3.2.1. For (i,j), after the index, giving the
     * maximum column capacity.
     *
     * @param degrees
     * @return upper triangular matrix
     */
    public static int[][] upperTriangularC() {
        C = new int[hIndex][hIndex];
        for (int i = 0; i < hIndex; i++) {
            for (int j = i + 1; j < hIndex; j++) {
                C[i][j] = Math.min(degrees[j], Csum(i + 1, j));
            }
        }
        return C;
    }

    /**
     * Make diagonal entries zeros
     *
     * @param mat int[][]
     */
    public static void zeroDiagonal(int[][] mat) {
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                if (i == j) {
                    mat[i][j] = 0;
                }
            }
        }
    }

    /**
     * Generates structures for the degrees, set after hydrogen distribution.
     *
     * @param degrees ArrayList<Integer> degree
     * @throws IOException
     * @throws CloneNotSupportedException
     * @throws CDKException
     */

    /**
     * deneme public static void gen(ArrayList<Integer> degrees) throws IOException,
     * CloneNotSupportedException, CDKException { int r=0; int[][] A = new int[size][size]; max =
     * maximalMatrix(degrees); L = upperTriangularL(degrees); C = upperTriangularC(degrees);
     *
     * <p>ArrayList<Integer> indices= new ArrayList<Integer>(); indices.add(0); indices.add(1);
     * forwardBuild(r,degrees,inputPartition,A,max,L,C,indices); }*
     */

    /**
     * Functions to detect x,y,z row indices of a block as explained in Grund' thesis.
     *
     * @param r int block index
     * @return
     */
    public static int findX(int r) {
        if (r == 0) {
            return 0;
        } else {
            return (sum(inputPartition, (r - 1)) - 1);
        }
    }

    public static int findY(int r) {
        return (sum(initialPartition, (r - 1)));
    }

    public static int findZ(int r) {
        return (sum(initialPartition, r) - 1);
    }

    /**
     * Grund Thesis 3.2.1 - Page 35 L and C Inverse formulas given at the bottom of the page.
     *
     * @param degrees atom valences
     * @param i row index
     * @param j column index
     * @param A matrix in the generation process
     * @return
     */
    public static int LInverse(int[] degrees, int i, int j, int[][] A) {
        int sum = 0;
        for (int s = 0; s < j; s++) {
            sum = sum + A[i][s];
        }
        return degrees[i] - sum;
    }

    public static int CInverse(int[] degrees, int i, int j, int[][] A) {
        int sum = 0;
        for (int s = 0; s < i; s++) {
            sum = sum + A[s][j];
        }
        return degrees[j] - sum;
    }

    /**
     * Successor and predecessor functions for forward and backward functions.
     *
     * @param indices indices of the matrix
     * @param size matrix dimension
     * @return
     */
    public static ArrayList<Integer> successor(ArrayList<Integer> indices, int size) {
        int i0 = indices.get(0);
        int i1 = indices.get(1);
        ArrayList<Integer> modified = new ArrayList<Integer>();
        // if(i0!=size-2 && i1!=size-1) {
        if (i1 == size - 1) {
            modified.add(i0 + 1);
            modified.add(i0 + 2);
        } else if (i1 < size - 1) {
            modified.add(i0);
            modified.add(i1 + 1);
        }
        // }
        return modified;
    }

    public static ArrayList<Integer> predecessor(ArrayList<Integer> indices, int size) {
        int i0 = indices.get(0);
        int i1 = indices.get(1);
        ArrayList<Integer> modified = new ArrayList<Integer>();
        if (i0 == i1 - 1) {
            modified.add(i0 - 1);
            modified.add(size - 1);
        } else {
            modified.add(i0);
            modified.add(i1 - 1);
        }
        return modified;
    }

    /**
     * Forward function of the matrix generator.
     *
     * @param r int block index
     * @param degrees ArrayList<Integer> valences
     * @param partition ArrayList<Integer> atom partition
     * @param A int[][] adjacency matrix
     * @param max int[][] maximal matrix
     * @param L int[][] L matrix
     * @param C int[][] C matrix
     * @param indices ArrayList<Integer> entry indices
     * @throws IOException
     * @throws CloneNotSupportedException
     * @throws CDKException
     */

    /**
     * public static void forwardBuild(int r, ArrayList<Integer> degrees, ArrayList<Integer>
     * partition,int[][] A, int[][]max, int[][]L, int[][]C, ArrayList<Integer> indices) throws
     * IOException, CloneNotSupportedException, CDKException { int y=findY(r); int z=findZ(r); int
     * i=indices.get(0); int j=indices.get(1); int l2= LInverse(degrees,i,j,A); int c2=
     * CInverse(degrees,i,j,A); int minimal= Math.min(max[i][j],Math.min(l2,c2)); for(int
     * h=minimal;h>=0;h--) { if((l2-h<=L[i][j]) && (c2-h<=C[i][j])) { A[i][j]=A[j][i]=h;
     *
     * <p>if(i==(max.length-2) && j==(max.length-1)) { if(blockTest(r, A)) { backwardBuild(r,
     * degrees,partitionList.get(y),A, max, L, C, indices); } }else { ArrayList<Integer>
     * modified=successor(indices,max.length); if(modified.get(0)>z) { // If its higher than z
     * value. if(blockTest(r, A)) { r++; //z+1 forwardBuild(r, degrees, partitionList.get(z+1), A,
     * max, L, C, modified); } }else { forwardBuild(r, degrees, partitionList.get(y), A, max, L, C,
     * modified); } } }else { if(i==max.length-2 && j==max.length-1) { ArrayList<Integer>
     * modified=predecessor(indices, max.length); indices=modified; } backwardBuild(r, degrees,
     * partitionList.get(y),A, max, L, C, indices); } } }*
     */

    /**
     * public static void forwardBuild(int r, ArrayList<Integer> degrees, ArrayList<Integer>
     * partition,int[][] A, int[][]max, int[][]L, int[][]C, ArrayList<Integer> indices, PrintWriter
     * pWriter) throws IOException, CloneNotSupportedException, CDKException { int y=findY(r); int
     * z=findZ(r); pWriter.print("forward build r y z"+" "+r+" "+y+" "+z+"\n"); int
     * i=indices.get(0); int j=indices.get(1); pWriter.print("forward indices"+" "+i+" "+j+"\n");
     * int l2= LInverse(degrees,i,j,A); int c2= CInverse(degrees,i,j,A); int minimal=
     * Math.min(max[i][j],Math.min(l2,c2)); for(int h=minimal;h>=0;h--) { if((l2-h<=L[i][j]) &&
     * (c2-h<=C[i][j])) { A[i][j]=A[j][i]=h; if(i==(max.length-2) && j==(max.length-1)) {
     * pWriter.print("last entry"+"\n"); backwardBuild(r, degrees,partitionList.get(y),A, max, L, C,
     * indices, pWriter); }else { ArrayList<Integer> modified=successor(indices,max.length);
     * pWriter.print("successor"+" "+modified.get(0)+" "+modified.get(1)+"\n");
     * if(modified.get(0)>z) { // If its higher than z value. pWriter.print("forward block is done i
     * r y z"+" "+i+" "+r+" "+y+" "+z+" "+"matrix"+" "+Arrays.deepToString(A)+"\n"); if(blockTest(i,
     * r, A,pWriter)) { r++; forwardBuild(r, degrees, partitionList.get(z+1), A, max, L, C,
     * modified, pWriter); } }else { forwardBuild(r, degrees, partitionList.get(y), A, max, L, C,
     * modified, pWriter); } } }else { if(i==max.length-2 && j==max.length-1) { ArrayList<Integer>
     * modified=predecessor(indices, max.length); indices=modified; } backwardBuild(r, degrees,
     * partitionList.get(y),A, max, L, C, indices, pWriter); } } }*
     */

    /**
     * In backward function, updating the block index.
     *
     * @param r int block index
     * @param indices ArrayList<Integer> atom valences
     * @return
     */
    public static int updateR(int r, ArrayList<Integer> indices) {
        int y = findY(r);
        if (indices.get(0) < y) {
            return (r - 1);
        } else {
            return r;
        }
    }

    /**
     * After generating matrices, adding the hydrogen with respect to the pre-hydrogen distribution.
     *
     * @param A int[][] adjacency matrix
     * @param index int beginning index for the hydrogen setting
     * @return
     */
    public static int[][] addHydrogens(int[][] A, int index) {
        int hIndex = index;
        int hydrogen = 0;
        for (int i = 0; i < index; i++) {
            hydrogen = initialDegrees[i] - sum(A[i]);
            for (int j = hIndex; j < (hIndex + hydrogen); j++) {
                A[i][j] = 1;
                A[j][i] = 1;
            }
            if (hydrogen == 0) {
                // hIndex++;
            } else {
                hIndex = hIndex + hydrogen;
            }
        }
        return A;
    }

    /**
     * Summing entries of an array.
     *
     * @param array int[]
     * @return int sum
     */
    public static int sum(int[] array) {
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum = sum + array[i];
        }
        return sum;
    }

    /**
     * Summing entries of a list.
     *
     * @param list List<Integer>
     * @return int sum
     */
    public static int sum(List<Integer> list) {
        int sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum = sum + list.get(i);
        }
        return sum;
    }

    /**
     * Building an atom container for an adjacency matrix
     *
     * @param mat int[][] adjacency matrix
     * @return
     * @throws CloneNotSupportedException
     */
    public static IAtomContainer buildC(int[][] mat) throws CloneNotSupportedException {
        IAtomContainer ac2 = atomContainer.clone();
        for (int i = 0; i < mat.length; i++) {
            for (int j = i + 1; j < mat.length; j++) {
                if (mat[i][j] == 1) {
                    ac2.addBond(i, j, Order.SINGLE);
                    ;
                } else if (mat[i][j] == 2) {
                    ac2.addBond(i, j, Order.DOUBLE);
                } else if (mat[i][j] == 3) {
                    ac2.addBond(i, j, Order.TRIPLE);
                }
            }
        }

        ac2 = AtomContainerManipulator.removeHydrogens(ac2);
        return ac2;
    }

    /**
     * Building an atom container from a string of atom-implicit hydrogen information. If provided,
     * fragments are also added.
     *
     * @param mol molecular information
     * @return atom container new atom container
     * @throws IOException
     * @throws CloneNotSupportedException
     * @throws CDKException
     */
    public static void build(String mol)
            throws IOException, CloneNotSupportedException, CDKException {
        for (int i = 0; i < symbols.size(); i++) {
            for (int j = 0; j < occurrences.get(i); j++) {
                atomContainer.addAtom(new Atom(symbols.get(i)));
            }
        }

        for (IAtom atom : atomContainer.atoms()) {
            atom.setImplicitHydrogenCount(0);
        }
    }

    /**
     * Cloning an atom container
     *
     * @param mol IAtomContainer
     * @return
     * @throws IOException
     * @throws CloneNotSupportedException
     * @throws CDKException
     */
    public static IAtomContainer clone(IAtomContainer mol)
            throws IOException, CloneNotSupportedException, CDKException {
        IAtomContainer mol2 = builder.newInstance(IAtomContainer.class);
        for (IAtom a : mol.atoms()) {
            mol2.addAtom(a);
        }
        for (IBond b : mol.bonds()) {
            mol2.addBond(b);
        }
        return mol2;
    }

    /** Molecule depiction */
    public static void depict(IAtomContainer molecule, String path)
            throws CloneNotSupportedException, CDKException, IOException {
        DepictionGenerator depiction = new DepictionGenerator();
        depiction.withSize(1000, 1000).withZoom(20).depict(molecule).writeTo(path);
    }

    /**
     * Backward function in the matrix generator.
     *
     * @param r int block index
     * @param degrees ArrayList<Integer> valences
     * @param partition ArrayList<Integer> atom partition
     * @param A int[][] adjacency matrix
     * @param max int[][] maximal matrix
     * @param L int[][] L matrix
     * @param C int[][] C matrix
     * @param indices ArrayList<Integer> entry indices
     * @throws IOException
     * @throws CloneNotSupportedException
     * @throws CDKException
     */

    /**
     * public static void backwardBuild(int r, ArrayList<Integer> degrees,ArrayList<Integer>
     * partition,int[][] A, int[][]max, int[][]L, int[][]C, ArrayList<Integer> indices) throws
     * IOException, CloneNotSupportedException, CDKException { r=updateR(r,indices); int y=findY(r);
     * int i=indices.get(0); int j=indices.get(1); int l2= LInverse(degrees,i,j,A); int c2=
     * CInverse(degrees,i,j,A); if(i==max.length-2 && j==max.length-1) { output.add(A); int[][]
     * mat2= new int[A.length][A.length]; for(int k=0;k<A.length;k++) { for(int l=0;l<A.length;l++)
     * { mat2[k][l]=A[k][l]; } } A=addHydrogens(A,hIndex); if(connectivityTest(hIndex,A)){
     * IAtomContainer mol= buildC(A); String inchi= inchiGeneration(mol);
     * if(!inchis.contains(inchi)) { outFile.write(clone(mol)); count++; inchis.add(inchi); } }
     *
     * <p>for(int h=0; h<representatives.size(); h++) {
     * representatives.get(h).removeAll(representatives.get(h)); } for(int k=(1);
     * k<partitionList.size(); k++) { partitionList.get(k).removeAll(partitionList.get(k)); }
     *
     * <p>}else{ ArrayList<Integer> modified=predecessor(indices, max.length); i= modified.get(0);
     * j= modified.get(1); if(i>0 && j-i==1) { int x= A[i][j]; if(x>0 && (l2-(x-1)<=L[i][j]) &&
     * (c2-(x-1)<=C[i][j])) { A[i][j]=A[j][i]=x-1; ArrayList<Integer>
     * modified2=successor(modified,max.length); forwardBuild(r, degrees, partitionList.get(y), A,
     * max, L, C, modified2); }else { backwardBuild(r, degrees,partitionList.get(y), A, max, L, C,
     * modified); } } } }*
     */

    /**
     * Testing every row in the block with the canonical test.
     *
     * @param index int row index
     * @param r int the block number
     * @param A int[][] adjacency matrix
     * @param partition ArrayList<Integer> atom partition
     * @param newPartition ArrayList<Integer> canonical partition
     * @return boolean
     */
    public static boolean blockTest(int r, int[][] A) {
        boolean check = true;
        int y = findY(r);
        int z = findZ(r);
        for (int i = y; i <= z; i++) {
            // pWriter.println("ilk"+" "+i+" "+partitionList.get(i));
            // pWriter.println("can"+" "+i+" "+canonicalPartition(i,partitionList.get(i)));
            // ArrayList<Integer> partition=canonicalPartition(i,partitionList.get(i));
            // if(!allis1(partitionList.get(i))) {
            // if(!block(i, A)){
            // if(!block(i,r, A, partitionList.get(i),canonicalPartition(i,partitionList.get(i)))){
            // if(!blockAr(newReps, i, r, A, partitionList.get(i),
            // canonicalPartition(i,partitionList.get(i)))){
            // if(!blockTestRow(i, r, A,
            // partitionList.get(i),canonicalPartition(i,partitionList.get(i)))){
            if (!blockTestPerm(
                    i, r, A, partitionList.get(i), canonicalPartition(i, partitionList.get(i)))) {
                check = false;
                break;
            }
            /** }else { check=true; break; }* */
        }

        clearFormers(check, y);
        // clearAr(check, y);
        return check;
    }

    /**
     * If the blockTest is false, clear the reps and representatives of the tested block.
     *
     * @param check boolean blockTest
     * @param y int beginning index of the block.
     */
    public static void clear(boolean check, int y) {
        if (check == false) {
            for (int i = y; i < representatives.size(); i++) {
                representatives.get(i).removeAll(representatives.get(i));
            }
            for (int i = y + 1; i < partitionList.size(); i++) {
                partitionList.get(i).removeAll(partitionList.get(i));
            }
        }
    }

    public static void clearFormers(boolean check, int y) {
        if (check == false) {
            for (int i = y; i < formerPermutations.size(); i++) {
                // deneme formerPermutations.get(i).removeAll(formerPermutations.get(i));
            }
            for (int i = y + 1; i < partitionList.size(); i++) {
                partitionList.get(i).removeAll(partitionList.get(i));
            }
        }
    }

    /**
     * For array perms
     *
     * @param check
     * @param y
     */
    // public static List<List<int[]>> repL= new ArrayList<List<int[]>>();
    /**
     * public static void clearAr(boolean check, int y) { pWriter.println("clear y"+" "+y+"
     * "+check); if(check==false) { for(int i=y;i<repL.size();i++) {
     * repL.get(i).removeAll(repL.get(i)); } for(int i=y+1;i<partitionList.size();i++) {
     * partitionList.get(i).removeAll(partitionList.get(i)); } } }*
     */
    public static List<List<List<int[]>>> repL = new ArrayList<List<List<int[]>>>();

    public static void clearAr(boolean check, int y) {
        if (check == false) {
            for (int i = y; i < repL.size(); i++) {
                repL.get(i).removeAll(repL.get(i));
            }
            for (int i = y + 1; i < partitionList.size(); i++) {
                partitionList.get(i).removeAll(partitionList.get(i));
            }
            formers.clear();
        }
    }

    public static int sum(ArrayList<Integer> list, int index) {
        int sum = 0;
        for (int i = 0; i <= index; i++) {
            sum = sum + list.get(i);
        }
        return sum;
    }

    /**
     * Grund Thesis 3.3.3. To calculate the number of conjugacy classes, used in cycle transposition
     * calculation.
     *
     * @param partEx ArrayList<Integer> former atom partition
     * @param degree ArrayList<Integer> atom valences
     * @return
     */
    public static int LValue(ArrayList<Integer> partEx, int degree) {
        return (sum(partEx, (degree))
                - (degree)); // In Grund, the numeration starts with 1. Since we start with 0, it
        // should be -(degree+1)+1, so just -degree
    }

    /**
     * Grund Thesis 3.3.3. To generate cycle transpositions for two Young subgroups.
     *
     * @param index int row index
     * @param partition ArrayList<Integer> atom partition
     * @return
     */
    public static List<Permutation> cycleTranspositions(int index, ArrayList<Integer> partition) {
        int total = sum(partition);
        List<Permutation> perms = new ArrayList<Permutation>();
        int lValue = LValue(partition, index);
        for (int i = 0; i < lValue; i++) {
            int[] values = idValues(total);
            int former = values[index];
            values[index] = values[index + i];
            values[index + i] = former;
            Permutation p = new Permutation(values);
            perms.add(p);
        }
        return perms;
    }

    public static List<int[]> cycleTransAr(int index, ArrayList<Integer> partition) {
        int total = sum(partition);
        List<int[]> perms = new ArrayList<int[]>();
        int lValue = LValue(partition, index);
        for (int i = 0; i < lValue; i++) {
            int[] values = idValues(total);
            int former = values[index];
            values[index] = values[index + i];
            values[index + i] = former;
            perms.add(values);
        }
        return perms;
    }
    /**
     * Within block test, testing whether the row is in maximal form or not.
     *
     * @param index int row index
     * @param r int block index
     * @param A int[][] adjacency matrices
     * @param partition ArrayList<Integer> atom partition
     * @param newPartition ArrayList<Integer> new atom partition after canonical repartitioning
     * @return
     */
    /**
     * deneme public static boolean block(int index, int r,int[][] A, ArrayList<Integer> partition,
     * ArrayList<Integer> newPartition) {
     */
    /**
     * boolean check=true; ArrayList<Integer> partition=
     * canonicalPartition(index,partitionList.get(index)); if(!allis1(partition)) {
     * if(!descendingOrderCheck(index,A[index], partition)){ check=false; }else {
     * if(!compareWithFormers(0, index, A, partition)) { check=false; }else { addPartition(index,
     * partition, A); } } } return check;*
     */
    /**
     * addRepresentatives(index,idPermutation(6)); int y = findY(r); boolean check= true; int total
     * = sum(partition); ArrayList<Permutation> cycleTrans = cycleTranspositions(index,partition);
     * ArrayList<Permutation> formerPermutations= formerPermutations( false,index, y, total);
     *
     * <p>for(Permutation cycle: cycleTrans) { /** Cycle id is not considered in the 3.3.19 since it
     * was from the first row in the block. Otherwise, without considering id element, I got many
     * duplicates.
     */

    /**
     * boolean formerCheck = formerPermutationsCheck(index,y, total, A, partition,
     * newPartition,formerPermutations, cycle,false); if(!formerCheck) { check=false; break; }else {
     * addPartition(index, newPartition, A); } } return check; }*
     */
    public static int[] idArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }

    public static boolean blockAr(
            List<List<int[]>> newReps,
            int index,
            int r,
            int[][] A,
            ArrayList<Integer> partition,
            ArrayList<Integer> newPartition) {
        if (index < 1) {
            addRepsAr(index, idArray(6));
        }
        int y = findY(r);
        boolean check = true;
        int total = sum(partition);
        List<int[]> cycleTrans = cycleTransAr(index, partition);
        // List<List<int[]>> formerPermutations= formPermsInBlockArr(index, y, total);
        // deneme setFormers(index,y,total);

        List<List<int[]>> formerPermutations = formers;
        List<int[]> formerList = new ArrayList<int[]>();
        for (int i = 0; i < formerPermutations.size(); i++) {
            formerList = formerPermutations.get(i);
        }

        for (int i = 0; i < cycleTrans.size(); i++) {
            // if(!idArrayCheck(cycle)) {
            boolean formerCheck =
                    formPermCheckAr(
                            newReps,
                            i,
                            index,
                            y,
                            total,
                            A,
                            partition,
                            newPartition,
                            formerPermutations,
                            cycleTrans.get(i));
            if (!formerCheck) {
                check = false;
                break;
            } else {
                addPartition(index, newPartition, A);
            }
            // }
        }
        return check;
    }

    public static boolean blockTestRow(
            int index,
            int r,
            int[][] A,
            ArrayList<Integer> partition,
            ArrayList<Integer> newPartition) {
        /** if(index<1) { addRepsAr(index,idArray(6)); }* */
        int y = findY(r);
        boolean check = true;
        int total = sum(partition);
        List<Permutation> cycleTrans = cycleTranspositions(index, partition);
        // List<int[]> cycleTrans = cycleTransAr(index, partition);
        // List<List<int[]>> formerPermutations= formPermsInBlockArr(index, y, total);
        // deneme setFormers(index,y,total);

        List<List<int[]>> formerPermutations = formers;
        List<int[]> formerList = new ArrayList<int[]>();
        for (int i = 0; i < formerPermutations.size(); i++) {
            formerList = formerPermutations.get(i);
        }

        addRepsAr(index, idArray(total));
        boolean cycleCheck = cyclemCheck(index, y, total, A, partition, newPartition, cycleTrans);
        if (!cycleCheck) {
            check = false;
        } else {
            addPartition(index, newPartition, A);
        }
        return check;
    }

    public static boolean blockTestPerm(
            int index,
            int r,
            int[][] A,
            ArrayList<Integer> partition,
            ArrayList<Integer> newPartition) {
        int y = findY(r);
        boolean check = true;
        int total = sum(partition);
        List<Permutation> cycleTrans = cycleTranspositions(index, partition);
        // setFormers(index,y,total);
        // deneme candidatePermutations(index,y,total,cycleTrans);
        // addReps(index, idPermutation(total));

        // boolean cycleCheck=cyclemCheck(index, y, total, A, partition, newPartition, cycleTrans);
        boolean cycleCheck = check(index, y, total, A, partition, newPartition);
        if (!cycleCheck) {
            check = false;
        } else {
            addPartition(index, newPartition, A);
        }
        return check;
    }

    public static boolean compareWithFormers(
            int y, int index, int[][] A, ArrayList<Integer> partition) {
        boolean check = true;
        int[] currentRow = A[index];
        for (int i = y; i < index; i++) {
            if (!descBlockLineCheck(index, A[i], currentRow, partition)) {
                check = false;
            }
        }
        return check;
    }

    public static boolean allis1(ArrayList<Integer> partition) {
        boolean check = true;
        for (int i = 0; i < partition.size(); i++) {
            if (partition.get(i) != 1) {
                check = false;
                break;
            }
        }
        return check;
    }

    /**
     * Updating canonical partition list.
     *
     * @param index row index
     * @param newPartition atom partition
     * @param A int[][] adjacency matrix
     */
    public static void addPartition(int index, ArrayList<Integer> newPartition, int[][] A) {
        ArrayList<Integer> refinedPartition = new ArrayList<Integer>();
        if (allis1(newPartition)) {
            refinedPartition.add(1);
            refinedPartition.add(1);
            refinedPartition.add(1);
            refinedPartition.add(1);
            refinedPartition.add(1);
            refinedPartition.add(1);
        } else {
            refinedPartition = refinedPartitioning(newPartition, A[index]);
        }
        if (partitionList.size() == (index + 1)) {
            partitionList.add(refinedPartition);
        } else {
            partitionList.set(index + 1, refinedPartition);
        }
    }

    /**
     * Grund 3.3.8 In case if the row is canonical, with respect to former partition, we build
     * refined partitioning.
     *
     * @param partition ArrayList<Integer> atom partition
     * @param row int[] row
     * @return
     */
    public static ArrayList<Integer> refinedPartitioning(ArrayList<Integer> partition, int[] row) {
        ArrayList<Integer> refined = new ArrayList<Integer>();
        int index = 0;
        int count = 1;
        for (Integer p : partition) {
            if (p != 1) {
                for (int i = index; i < p + index - 1; i++) {
                    if (i + 1 < p + index - 1) {
                        if (row[i] == row[i + 1]) {
                            count++;
                        } else {
                            refined.add(count);
                            count = 1;
                        }
                    } else {
                        if (row[i] == row[i + 1]) {
                            count++;
                            refined.add(count);
                            count = 1;
                        } else {
                            refined.add(count);
                            refined.add(1);
                            count = 1;
                        }
                    }
                }
                index = index + p;
            } else {
                index++;
                refined.add(1);
                count = 1;
            }
        }
        return refined;
    }

    /**
     * To calculate former permutations within the block or the permutations of former block.
     *
     * @param formerBlocks boolean
     * @param index int row index
     * @param y int y index of the block
     * @param total int number of atoms
     * @return
     */
    public static ArrayList<Permutation> formerPermutations(
            boolean formerBlocks, int index, int y, int total) {
        if (formerBlocks) {
            return formerPermutationsFromFormerBlocks(y);
        } else {
            return formerPermutationsInBlock(index, y, total);
        }
    }

    /**
     * The multiplication of the former permutations from the former block.
     *
     * @param y int y index of the block.
     * @return
     */
    public static ArrayList<Permutation> formerPermutationsFromFormerBlocks(int y) {
        ArrayList<Permutation> list = representatives.get(0);
        ArrayList<Permutation> nList = new ArrayList<Permutation>();
        for (int i = 1; i < y; i++) {
            for (int l = 0; l < list.size(); l++) {
                for (Permutation perm : representatives.get(i)) {
                    Permutation mult = list.get(l).multiply(perm);
                    if (!nList.contains(mult)) {
                        nList.add(mult);
                    }
                }
            }
            list.clear();
            list.addAll(nList); // list=nList does not work.
            nList.clear();
        }
        return list;
    }

    /**
     * Like in Grund Example 3.3.19; We need to check with the permutations of the former
     * representatives. This function helps us to calculate the multiplication of the former
     * representatives. So, before the beginning of the current strip (or block).
     *
     * <p>These are M(i) members.
     *
     * @param index int current row index
     * @param y int beginning of the block
     * @return
     */
    public static ArrayList<Permutation> formerPermutationsInBlock(int index, int y, int total) {
        for (int s = y; s <= index; s++) {
            ArrayList<Permutation> list = representatives.get(s);
        }
        if (index == y) {
            ArrayList<Permutation> form = new ArrayList<Permutation>();
            form.add(idPermutation(total));
            return form;
        } else if (index == (y + 1)) {
            return representatives.get(y);
        } else {
            ArrayList<Permutation> list = representatives.get(y);
            ArrayList<Permutation> nList = new ArrayList<Permutation>();
            for (int l = 0; l < list.size(); l++) {
                Permutation yDen = list.get(l);
                for (int i = (y + 1); i < index; i++) {
                    for (Permutation perm : representatives.get(i)) {
                        Permutation mult = yDen.multiply(perm);
                        // if(!mult.isIdentity()) {
                        if (!nList.contains(mult)) {
                            nList.add(mult);
                        }
                        // }
                    }
                }
                /**
                 * list.clear(); list.addAll(nList); // list=nList does not work. nList.clear();*
                 */
            }
            return nList;
        }
    }

    /**
     * deneme public static List<List<int[]>> formPermsInBlockArr(int index, int y, int total) {
     * List<List<int[]>> nList= new ArrayList<List<int[]>>(); for(int s=y;s<=index;s++) {
     * List<int[]> list= repL.get(s); } if(index==y) { List<int[]> form= new ArrayList<int[]>();
     * form.add(idArray(total)); nList.add(form); return nList; }else if(index==(y+1)){ for(int[]
     * array: repL.get(y)) { List<int[]> add= new ArrayList<int[]>(); add.add(array);
     * nList.add(add); } return nList; }else { List<int[]> list1= repL.get(y); for(int
     * l=0;l<list1.size();l++) { //ilk list 1 den alp dierleri ile carp int[] array= list1.get(l);
     * ArrayList<int[]> lim = new ArrayList<int[]>(); lim.add(array); for(int i=(y+1);i<index;i++) {
     * //System.out.println("o indexdeki arrayler"+" "+i); for(int[] perm: repL.get(i)) {
     * lim.add(perm); } } nList.add(lim); } return nList; } }*
     */
    public static List<List<int[]>> formers = new ArrayList<List<int[]>>();
    /**
     * public static void setFormers(int index, int y, int total) { List<int[]> form= new
     * ArrayList<int[]>(); form.add(idArray(size)); formers.add(form); }*
     */

    /**
     * public static void setFormers(int index, int y, int total) { pWriter.println("setFormers
     * icinde"); List<List<int[]>> nList= new ArrayList<List<int[]>>(); if(index==y) {
     * pWriter.println("index=y"); List<int[]> form= new ArrayList<int[]>();
     * form.add(idArray(total)); nList.add(form); formers= nList; pWriter.println("fotmerlar");
     * for(List<int[]> l: formers) { for(int[] a: l) { pWriter.println("bu"+" "+Arrays.toString(a));
     * } } pWriter.println("former bitti"); }else if(index==(y+1)){ pWriter.println("index=y+1");
     * pWriter.println("repLer"); for(List<int[]> l: repL.get(y)) { for(int[] a: l) {
     * pWriter.println("rep"+" "+Arrays.toString(a)); } } pWriter.println("repler bitti");
     * nList.addAll(repL.get(y)); formers= nList; pWriter.println("fotmerlar"); for(List<int[]> l:
     * formers) { for(int[] a: l) { pWriter.println("bu"+" "+Arrays.toString(a)); } }
     * pWriter.println("former bitti"); }else { pWriter.println("index>y"); List<List<int[]>>
     * lastList= repL.get(index-1); //nList.addAll(formers); for(List<int[]> list: formers) {
     * for(List<int[]> add: lastList) { List<int[]> newList= cloneList(list); //newList.addAll(add);
     * addWithoutID(newList, add); nList.add(newList); } } formers= nList;
     * pWriter.println("fotmerlar"); for(List<int[]> l: formers) { for(int[] a: l) {
     * pWriter.println("bu"+" "+Arrays.toString(a)); } } pWriter.println("former bitti"); } }*
     */

    /**
     * deneme public static void setFormers(int index, int y, int total) { List<Permutation> nList=
     * new ArrayList<Permutation>(); if(index==y) { nList.add(idPermutation(total));
     * formerPermutations= nList; }else if(index==(y+1)){ nList.addAll(representatives.get(y));
     * formerPermutations= nList; }else { /**pWriter.println("index>y"); List<Permutation> lastList=
     * representatives.get(index-1); for(Permutation form: formerPermutations) { for(Permutation
     * last: lastList) { nList.add(last.multiply(form)); } } formerPermutations= nList;
     * pWriter.println("formerlar"); for(Permutation perm: formerPermutations) {
     * pWriter.println("former"+" "+perm.toCycleString()); } pWriter.println("former bitti");*
     */
    // }
    // }

    /**
     * public static void candidatePermutations(int index, int y, int total, List<Permutation>
     * cycles) { List<Permutation> nList= new ArrayList<Permutation>(); nList.addAll(cycles);
     * if(index==y) { formerPermutations= nList; }else{ for(Permutation form: formerPermutations) {
     * if(!form.isIdentity()) { nList.add(form); } } List<Permutation> newForm = new
     * ArrayList<Permutation>(); for(Permutation frm: formerPermutations) { if(!frm.isIdentity()) {
     * newForm.add(frm); } } List<Permutation> newCycles = new ArrayList<Permutation>();
     * for(Permutation cyc: cycles) { if(!cyc.isIdentity()) { newCycles.add(cyc); } }
     * for(Permutation perm: newForm) { for(Permutation cycle: newCycles) { Permutation
     * newPermutation =cycle.multiply(perm); if(!newPermutation.isIdentity()) {
     * nList.add(newPermutation); } } } formerPermutations= nList; } }*
     */

    /**
     * deneme
     *
     * <p>public static void candidatePermutations(int index, int y, int total, List<Permutation>
     * cycles) { List<Permutation> newList= new ArrayList<Permutation>(); newList.addAll(cycles);
     * formerPermutations.add(index,newList); if(index!=0) { List<Permutation> formers =
     * formerPermutations.get(index-1); for(Permutation form: formers) { if(!form.isIdentity()) {
     * formerPermutations.get(index).add(form); } } List<Permutation> newForm = new
     * ArrayList<Permutation>(); for(Permutation frm: formers) { if(!frm.isIdentity()) {
     * newForm.add(frm); } } List<Permutation> newCycles = new ArrayList<Permutation>();
     * for(Permutation cyc: cycles) { if(!cyc.isIdentity()) { newCycles.add(cyc); } }
     * for(Permutation perm: newForm) { for(Permutation cycle: newCycles) { Permutation
     * newPermutation =cycle.multiply(perm); if(!newPermutation.isIdentity()) {
     * formerPermutations.get(index).add(newPermutation); } } } } }*
     */
    public static void addWithoutID(List<int[]> list, List<int[]> list2) {
        for (int[] array : list2) {
            if (!idArrayCheck(array)) {
                list.add(array);
            }
        }
    }

    public static List<int[]> cloneList(List<int[]> list) {
        List<int[]> newList = new ArrayList<int[]>();
        for (int[] array : list) {
            newList.add(array);
        }
        return newList;
    }

    public static List<int[]> cloneWithoutList(List<int[]> list) {
        List<int[]> newList = new ArrayList<int[]>();
        for (int[] array : list) {
            if (!idArrayCheck(array)) {
                newList.add(array);
            }
        }
        return newList;
    }

    public static void multiplyFormers(List<int[]> automorphisms) {
        List<List<int[]>> newFormers = new ArrayList<List<int[]>>();
        for (int l = 0; l < formers.size(); l++) {
            for (int[] automorphism : automorphisms) {
                List<int[]> newBranch = cloneList(formers.get(l));
                newBranch.add(automorphism);
                newFormers.add(newBranch);
            }
        }
        formers = newFormers;
    }

    /**
     * oncelike biz cycle i test edip sonrasnda automorphsim varsa carpmalyz. cycle id den dolayi
     * gelecek.
     *
     * <p>sonrasnda former icin test edilebilir. yada cycle olmadan da once bir former test
     * edilebilir. sonucta o satrda da ilk olarak id olacak perm. formern autosu former a eklenmeli
     * satra deil yoksa carpmda baskalar ile de ekilesim olur. hata verir.
     */
    public static void updateFormers(int index, int[] oldPermutation, int[] newPermutation) {
        formers.get(index).remove(oldPermutation);
        formers.get(index).add(newPermutation);
    }

    /**
     * To add a permutation to representatives.
     *
     * @param index representatives table index
     * @param perm Permutation
     */
    public static void addReps(int index, Permutation perm) {
        if (representatives.size() == index) {
            ArrayList<Permutation> perms = new ArrayList<Permutation>();
            perms.add(perm);
            representatives.add(perms);
        } else {
            if (!representatives.get(index).contains(perm)) {
                representatives.get(index).add(perm);
            }
        }
    }

    /**
     * for perms we keep them as how they are. For int[] version of perms we need triple list rep of
     * repL.
     */
    /**
     * addRepresentatives as array
     *
     * @param index
     * @param perm
     */
    /**
     * public static void addRepsAr(int index, int[] perm) { if(repL.size()==index) { List<int[]>
     * perms = new ArrayList<int[]>(); perms.add(perm); repL.add(perms); }else {
     * //if(!repL.get(index).contains(perm)) { if(!searchArray(index, perm)) {
     * repL.get(index).add(perm); } } }*
     */
    public static void addRepsAr(int index, int[] perm) {
        if (repL.size() == index) {
            List<List<int[]>> perms = new ArrayList<List<int[]>>();
            List<int[]> rep = new ArrayList<int[]>();
            rep.add(perm);
            perms.add(rep);
            repL.add(perms);
        } else {
            // if(!repL.get(index).contains(perm)) {
            // if(!searchArray(index, perm)) {
            List<int[]> rep = new ArrayList<int[]>();
            rep.add(perm);
            repL.get(index).add(rep);
            // }
        }
    }

    /**
     * public static boolean searchArray(int index, int[] permutation) { boolean check=false;
     * for(int[] array: repL.get(index)) { if(Arrays.equals(array, permutation)) { check=true;
     * break; } } return check; }*
     */

    /**
     * To calculated the automorphism of a row, so it is still in maximal form and the permutation
     * maps row to itself.
     *
     * @param index int row index
     * @param y int y index of the block
     * @param total int number of atoms
     * @param A int[][] adjacency matrices
     * @param partition ArrayList<Integer> atom partition
     * @param newPartition ArrayList<Integer> canonical atom partition
     * @param former Permutation former permutation
     * @param cycle Permutation cycle coming from Grund 3.3.3.
     * @return
     */
    public static boolean equalAuto = true;
    // getCanonicalPermutatiom(index, y, total, A, partition, newPartition, cycleTransposition);
    public static Permutation getCanonicalPermutatiom(
            int index,
            int y,
            int total,
            int[][] A,
            ArrayList<Integer> partition,
            ArrayList<Integer> newPartition,
            Permutation cycleTransposition) {
        equalAuto = true;
        biggest = true;
        Permutation canonicalPermutation = idPermutation(total);
        // if(descBlockwiseCheck2(index,y,A,newPartition)) {
        // if(!equalBlockCheck(newPartition,index,y,A,cycleTransposition,formerPermutation)) {
        if (!equalBlockCheck(newPartition, index, y, A, cycleTransposition, idPermutation(6))) {
            // canonicalPermutation=getEqualPermutation(cycleTransposition, formerPermutation,
            // index, y, total,A, partition, newPartition);
            canonicalPermutation =
                    getEqualPermutation(
                            cycleTransposition, index, y, total, A, partition, newPartition);
            // original=actArray(actArray(actArray(A[cycleTransposition.get(index)],cycleTransposition),formerPermutation),perm);
            // Permutation mult = formerPermutation.multiply(cycleTransposition); former ile
            // int[] check =
            // actArray(actArray(actArray(A[mult.get(index)],cycleTransposition),formerPermutation),canonicalPermutation); former ile index belirle
            int[] check =
                    actArray(
                            actArray(A[cycleTransposition.get(index)], cycleTransposition),
                            canonicalPermutation);
            /** Cycle trans belirliyordu index */
            if (canonicalPermutation.isIdentity()) {
                if (!descendingOrdercomparison(newPartition, A[index], check)) {
                    // if(!descBlockLineCheck(index, A[index], check, newPartition)){ former da
                    // kullandim
                    equalAuto = false;
                }
            }
        }
        // }
        return canonicalPermutation;
    }

    public static int[] getCanonicalArray(
            int index,
            int y,
            int total,
            int[][] A,
            ArrayList<Integer> partition,
            ArrayList<Integer> newPartition,
            int[] cycleTransposition,
            List<int[]> formers) {
        equalAuto = true;
        biggest = true;
        int[] canonicalPermutation = idArray(total);
        if (!equalBlockCycleCheck(
                newPartition, index, y, A, cycleTransposition, formers, canonicalPermutation)) {
            canonicalPermutation =
                    getEqualPermCycle(
                            formers,
                            cycleTransposition,
                            index,
                            y,
                            total,
                            A,
                            partition,
                            newPartition);
            int[] check = row2compare(index, A, cycleTransposition, formers);
            /**
             * int[] check = A[findIndex(cycleTransposition[index],formers)];
             * check=formerCyclesActions(cycleActions(check,cycleTransposition), formers);*
             */
            check = cycleActions(check, canonicalPermutation);
            if (idArrayCheck(canonicalPermutation)) {
                if (!descendingOrdercomparison(newPartition, A[index], check)) {
                    equalAuto = false;
                }
            }
        }
        return canonicalPermutation;
    }

    public static int[] getCanonicalCycle(
            int index,
            int y,
            int total,
            int[][] A,
            ArrayList<Integer> partition,
            ArrayList<Integer> newPartition,
            int[] cycleTransposition) {
        equalAuto = true;
        biggest = true;
        int[] canonicalPermutation = idArray(total);
        if (!equalBlockCycleCheck(
                newPartition, index, y, A, cycleTransposition, canonicalPermutation)) {
            canonicalPermutation =
                    getEqualPermCycle(
                            cycleTransposition, index, y, total, A, partition, newPartition);
            int[] check = row2compare(index, A, cycleTransposition);
            check = cycleActions(check, canonicalPermutation);
            if (idArrayCheck(canonicalPermutation)) {
                if (!descendingOrdercomparison(newPartition, A[index], check)) {
                    equalAuto = false;
                }
            }
        }
        return canonicalPermutation;
    }

    public static Permutation getCanonicalCycle(
            int index,
            int y,
            int total,
            int[][] A,
            ArrayList<Integer> partition,
            ArrayList<Integer> newPartition,
            Permutation cycleTransposition) {
        equalAuto = true;
        biggest = true;
        Permutation canonicalPermutation = idPermutation(total);
        if (!equalBlockCheck(newPartition, index, y, A, cycleTransposition, canonicalPermutation)) {
            canonicalPermutation =
                    getEqualPerm(cycleTransposition, index, y, total, A, partition, newPartition);
            int[] check = row2compare(index, A, cycleTransposition);
            check = actArray(check, canonicalPermutation);
            if (canonicalPermutation.isIdentity()) {
                if (!descendingOrdercomparison(newPartition, A[index], check)) {
                    equalAuto = false;
                }
            }
        }
        return canonicalPermutation;
    }

    public static boolean idArrayCheck(int[] array) {
        boolean check = true;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != i) {
                check = false;
                break;
            }
        }
        return check;
    }
    /**
     * public static boolean biggerCheck(int index, int[] original, int[] permuted,
     * ArrayList<Integer> partition) { pWriter.println("bigger check");
     * pWriter.println("partition"+" "+partition); pWriter.println("original"+"
     * "+Arrays.toString(original)); pWriter.println("permuted"+" "+Arrays.toString(permuted));
     * int[] temp = cloneArray(permuted); temp=descendingSortWithPartition(temp, partition);
     * pWriter.println("permuted desc sort"+" "+Arrays.toString(temp)); return
     * descendingOrdercomparison(index, partition, original, temp); }*
     */
    public static boolean biggerCheck(
            int index, int[] original, int[] permuted, ArrayList<Integer> partition) {
        permuted = descendingSortWithPartition(permuted, partition);
        return descendingOrderUpperMatrix(index, partition, original, permuted);
        // return descendingOrdercomparison(partition, original, permuted);
    }

    public static boolean equalSetCheck(
            int[] original, int[] permuted, ArrayList<Integer> partition) {
        int[] temp = cloneArray(permuted);
        temp = descendingSortWithPartition(temp, partition);
        return equalSetCheck(partition, original, temp);
    }

    public static int[] descendingSortWithPartition(int[] array, ArrayList<Integer> partition) {
        int i = 0;
        for (Integer p : partition) {
            array = descendingSort(array, i, i + p);
            i = i + p;
        }
        return array;
    }

    public static int[] descendingSort(int[] array, int index0, int index1) {
        int temp = 0;
        for (int i = index0; i < index1; i++) {
            for (int j = i + 1; j < index1; j++) {
                if (array[i] < array[j]) {
                    temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }
        return array;
    }

    /**
     * This function performs the canonical test with the former representatives. That can be the
     * reps from the former representatives in the block. If it outputs the canonical permutation as
     * id, then not passed the canonical test and remove already added new entries from the list of
     * representatives.
     *
     * @param index int row index
     * @param y int the first index of the block
     * @param total int number of atoms
     * @param A int[][] adjacency matrix
     * @param partition ArrayList<Integer> atom partition
     * @param newPartition ArrayList<Integer> canonical partition
     * @param perm Permutation perm
     * @param formerBlocks boolean true if permutations are from former blocks
     * @return Permutation
     */
    public static boolean formerPermutationsCheck(
            int index,
            int y,
            int total,
            int[][] A,
            ArrayList<Integer> partition,
            ArrayList<Integer> newPartition,
            ArrayList<Permutation> formerPermutations,
            Permutation cycleTransposition,
            boolean formerBlocks) {
        boolean check = true;
        Permutation canonicalPermutation = idPermutation(total);
        canonicalPermutation =
                getCanonicalPermutatiom(
                        index, y, total, A, partition, newPartition, cycleTransposition);
        if (biggest) {
            for (Permutation formerPermutation : formerPermutations) {
                // Permutation testPermutation= formerPermutation.multiply(cycleTransposition);
                // former olanda
                Permutation testPermutation = formerPermutation.multiply(cycleTransposition);
                int[] array =
                        actArray(
                                actArray(
                                        actArray(
                                                A[cycleTransposition.get(index)],
                                                cycleTransposition),
                                        canonicalPermutation),
                                formerPermutation);
                /**
                 * int[] array = new int[6]; if(cycleTransposition.isIdentity()) { array=
                 * actArray(actArray(actArray(A[formerPermutation.multiply(cycleTransposition).get(index)],cycleTransposition),canonicalPermutation),formerPermutation);
                 * }else { array=
                 * actArray(actArray(actArray(A[cycleTransposition.get(index)],cycleTransposition),canonicalPermutation),formerPermutation);
                 * }*
                 */
                if (descendingOrdercomparison(newPartition, A[index], array)) {
                    if (canonicalPermutation.isIdentity()) {
                        if (descBlockwiseCheck2(index, y, A, newPartition)) {
                            // deneme addRepresentatives(index,idPermutation(6));
                        } else {
                            check = false;
                            break;
                        }
                    } else {
                        if (testPermutation.equals(
                                formerPermutation.multiply(
                                        cycleTransposition.multiply(canonicalPermutation)))) {
                            if (equalAuto) {
                                // deneme addRepresentatives(index, idPermutation(6));
                            } else {
                                check = false;
                                break;
                            }
                        } else {
                            // deneme addRepresentatives(index,
                            // cycleTransposition.multiply(canonicalPermutation));
                        }
                    }
                } else {
                    check = false;
                    break;
                }
            }
        } else {
            check = false;
        }
        return check;
    }

    /**
     * public static int[] row2compare(int index, int[][] A, int[] cycleTransposition, List<int[]>
     * formerPermutation) { pWriter.println("cycle"+" "+Arrays.toString(cycleTransposition));
     * pWriter.println("formers"); for(int i=formerPermutation.size()-1;i>=0;i--) {
     * pWriter.println("former perm"+" "+Arrays.toString(formerPermutation.get(i))); }
     * pWriter.println("index find"+" "+findIndex(cycleTransposition[index],formerPermutation));
     * pWriter.println("clone"+" "+Arrays.toString(A[1])); int[] array =
     * cloneArray(A[findIndex(cycleTransposition[index],formerPermutation)]); pWriter.println("array
     * to compare"+" "+Arrays.toString(array));
     * array=formerCyclesActions(cycleActions(array,cycleTransposition), formerPermutation); return
     * array; }*
     */
    public static int[] row2compare(
            int index, int[][] A, int[] cycleTransposition, List<int[]> formerPermutation) {
        int[] array = cloneArray(A[findIndex(index, cycleTransposition, formerPermutation)]);
        array = formerCyclesActions(cycleActions(array, cycleTransposition), formerPermutation);
        return array;
    }

    public static int[] row2compare(
            int index, int[][] A, Permutation cycleTransposition, Permutation formerPermutation) {
        int[] array = cloneArray(A[findIndex(index, cycleTransposition, formerPermutation)]);
        Permutation perm = formerPermutation.multiply(cycleTransposition);
        array = actArray(array, perm);
        return array;
    }

    public static int[] row2compare(int index, int[][] A, int[] cycleTransposition) {
        int[] array = cloneArray(A[cycleTransposition[index]]);
        array = cycleActions(array, cycleTransposition);
        return array;
    }

    public static int[] row2compare(int index, int[][] A, Permutation cycleTransposition) {
        int[] array = cloneArray(A[findIndex(index, cycleTransposition)]);
        array = actArray(array, cycleTransposition);
        return array;
    }

    public static int[] cloneArray(int[] array) {
        int length = array.length;
        int[] cloned = new int[length];
        for (int i = 0; i < length; i++) {
            cloned[i] = array[i];
        }
        return cloned;
    }

    public static boolean formPermCheckAr(
            List<List<int[]>> newReps,
            int cycleIndex,
            int index,
            int y,
            int total,
            int[][] A,
            ArrayList<Integer> partition,
            ArrayList<Integer> newPartition,
            List<List<int[]>> formerPermutations,
            int[] cycleTransposition) {
        boolean check = true;
        List<int[]> formerList = new ArrayList<int[]>();
        for (int i = 0; i < formerPermutations.size(); i++) {
            formerList = formerPermutations.get(i);
        }

        for (List<int[]> formerPermutation : formerPermutations) {
            int[] array = row2compare(index, A, cycleTransposition, formerPermutation);
            int[] canonicalPermutation = new int[total];
            canonicalPermutation =
                    getCanonicalArray(
                            index,
                            y,
                            total,
                            A,
                            partition,
                            newPartition,
                            cycleTransposition,
                            formerPermutation);
            if (biggest) {
                int[] test = row2compare(index, A, cycleTransposition, formerPermutation);
                test = cycleActions(test, canonicalPermutation);
                // if(descendingOrdercomparison(newPartition,A[index], test)) {
                if (descendingOrderUpperMatrix(index, newPartition, A[index], test)) {
                    if (idArrayCheck(canonicalPermutation)) {
                        if (!alreadyAdded(newReps, formerPermutation)) {
                            newReps.add(formerPermutation);
                        }
                        // addRepsAr(index, idArray(total));
                    } else {
                        int[] newRep = multiply(cycleTransposition, canonicalPermutation);
                        List<int[]> former = cloneWithoutList(formerPermutation);
                        former.add(newRep);
                        if (!alreadyAdded(newReps, former)) {
                            newReps.add(former);
                        }
                        addRepsAr(index, newRep);
                    }
                } else {
                    check = false;
                    break;
                }
            } else {
                check = false;
                break;
            }
        }
        return check;
    }

    public static List<List<Permutation>> formerPermutation = new ArrayList<List<Permutation>>();
    public static List<Permutation> formerPermutations = new ArrayList<Permutation>();

    public static boolean formerCheck(
            int index,
            int y,
            int total,
            int[][] A,
            ArrayList<Integer> partition,
            ArrayList<Integer> newPartition,
            Permutation newRep) {
        boolean check = true;
        List<Permutation> formerList = new ArrayList<Permutation>();
        for (Permutation formerPermutation : formerPermutations) {
            // int[] array=row2compare(index, A, newRep, formerPermutation);
            // pWriter.println("after former cycle actions"+" "+Arrays.toString(array));
            /**
             * int[] test=row2compare(index, A, newRep, formerPermutation);
             * //pWriter.println("cycleAction onces"+" "+Arrays.toString(test)); //array=
             * formerCyclesActions(cycleActions(array,newRep), formerPermutation);*
             */
            // pWriter.println("after canonical perm action on the permuted:"+"
            // "+Arrays.toString(array));
            Permutation testPerm = newRep.multiply(formerPermutation);
            Permutation canonicalPermutation =
                    getCanonicalCycle(index, y, total, A, partition, newPartition, testPerm);
            int[] test = row2compare(index, A, newRep, formerPermutation);
            test = actArray(test, canonicalPermutation);
            if (descendingOrdercomparison(newPartition, A[index], test)) {
                // if(descendingOrderUpperMatrix(index,newPartition,A[index], test)) {
                if (canonicalPermutation.isIdentity()) {
                    if (equalCheck(A[index], test, partition)) {
                        formerList.add(formerPermutation);
                    }
                } else {
                    Permutation newRepr =
                            canonicalPermutation.multiply(formerPermutation.multiply(newRep));
                    formerList.add(newRepr);
                }
            } else {
                formerList.clear();
                check = false;
                break;
            }
            /**
             * if(!descendingOrderUpperMatrix(index,newPartition,A[index], array)) {
             * pWriter.println("desc comp false"); check=false; break; }*
             */
        }
        if (check) {
            formerPermutations = formerList;
        }
        return check;
    }

    public static boolean cycleCheck(
            int index,
            int y,
            int total,
            int[][] A,
            ArrayList<Integer> partition,
            ArrayList<Integer> newPartition,
            List<int[]> cycleTranspositions) {
        boolean check = true;
        for (int[] cycleTransposition : cycleTranspositions) {
            int[] array = row2compare(index, A, cycleTransposition);
            int[] canonicalPermutation = new int[total];
            canonicalPermutation =
                    getCanonicalCycle(
                            index, y, total, A, partition, newPartition, cycleTransposition);
            if (biggest) {
                int[] test = row2compare(index, A, cycleTransposition);
                test = cycleActions(test, canonicalPermutation);
                // if(descendingOrdercomparison(newPartition,A[index], test)) {
                if (descendingOrderUpperMatrix(index, newPartition, A[index], test)) {
                    if (idArrayCheck(canonicalPermutation)) {
                        // addRepsAr(index, idArray(total));
                        /**
                         * if(!formerCheck(index, A, newPartition, idArray(total))){ check=false;
                         * pWriter.println("id former olmad"); break; }*
                         */
                    } else {
                        int[] newRep = multiply(cycleTransposition, canonicalPermutation);
                        addRepsAr(index, newRep);
                        /**
                         * if(!formerCheck(index, A, newPartition, newRep)){ check=false;
                         * pWriter.println("non id former olmad"); break; } pWriter.println("after
                         * mult cycle:"+" "+Arrays.toString(cycleTransposition));*
                         */
                    }
                } else {
                    check = false;
                    break;
                }
            } else {
                check = false;
                break;
            }
        }
        return check;
    }

    /**
     * Permutation icin
     *
     * @param index
     * @param y
     * @param total
     * @param A
     * @param partition
     * @param newPartition
     * @param cycleTranspositions
     * @return
     */
    public static boolean cyclemCheck(
            int index,
            int y,
            int total,
            int[][] A,
            ArrayList<Integer> partition,
            ArrayList<Integer> newPartition,
            List<Permutation> cycleTranspositions) {
        boolean check = true;
        for (Permutation cycleTransposition : cycleTranspositions) {
            int[] array = row2compare(index, A, cycleTransposition);
            Permutation canonicalPermutation =
                    getCanonicalCycle(
                            index, y, total, A, partition, newPartition, cycleTransposition);
            if (biggest) {
                int[] test = row2compare(index, A, cycleTransposition);
                test = actArray(test, canonicalPermutation);
                // if(descendingOrdercomparison(newPartition,A[index], test)) {
                if (descendingOrdercomparison(newPartition, A[index], test)) {
                    if (canonicalPermutation.isIdentity()) {
                        // addRepsAr(index, idArray(total));
                        if (!formerCheck(
                                index,
                                y,
                                total,
                                A,
                                partition,
                                newPartition,
                                idPermutation(total))) {
                            check = false;
                            break;
                        }
                    } else {
                        Permutation newRep = cycleTransposition.multiply(canonicalPermutation);
                        addReps(index, newRep);
                        if (!formerCheck(index, y, total, A, partition, newPartition, newRep)) {
                            check = false;
                            break;
                        }
                    }
                } else {
                    check = false;
                    break;
                }
            } else {
                check = false;
                break;
            }
        }
        return check;
    }

    public static List<List<Permutation>> formPerm = new ArrayList<List<Permutation>>();

    public static boolean check(
            int index,
            int y,
            int total,
            int[][] A,
            ArrayList<Integer> partition,
            ArrayList<Integer> newPartition) {
        boolean check = true;
        List<Permutation> formerList = new ArrayList<Permutation>();
        /** bu formerPermutations List<List olarak idi. */
        for (Permutation permutation : formPerm.get(index)) {
            Permutation canonicalPermutation =
                    getCanonicalCycle(index, y, total, A, partition, newPartition, permutation);
            if (biggest) {
                int[] test = row2compare(index, A, permutation);
                test = actArray(test, canonicalPermutation);
                if (descendingOrdercomparison(newPartition, A[index], test)) {
                    if (canonicalPermutation.isIdentity()) {
                        if (equalCheck(A[index], test, partition)) {
                            formerList.add(permutation);
                        }
                    } else {
                        Permutation newPermutation = canonicalPermutation.multiply(permutation);
                        formerList.add(newPermutation);
                    }
                } else {
                    formerList.clear();
                    check = false;
                    break;
                }
            } else {
                formerList.clear();
                check = false;
                break;
            }
        }

        if (check) {
            // deneme formerPermutations.get(index).clear();
            // deneme formerPermutations.set(index, formerList);
        }
        return check;
    }

    public static boolean alreadyAdded(List<List<int[]>> formers, List<int[]> newFormer) {
        boolean check = false;
        for (int i = 0; i < formers.size(); i++) {
            if (equalLists(formers.get(i), newFormer)) {
                check = true;
                break;
            }
        }
        return check;
    }

    public static boolean equalLists(List<int[]> list1, List<int[]> list2) {
        boolean check = true;
        if (list1.size() != list2.size()) {
            check = false;
        } else {
            for (int i = 0; i < list1.size(); i++) {
                if (!Arrays.equals(list1.get(i), list2.get(i))) {
                    check = false;
                    break;
                }
            }
        }
        return check;
    }

    public static int[] multiply(int[] perm1, int[] perm2) {
        int permLength = perm1.length;
        int[] clone = cloneArray(perm1);
        for (int i = 0; i < permLength; i++) {
            if (i != perm2[i] && i < perm2[i]) {
                clone = actCycleOnArray(clone, i, perm2[i]);
            }
        }
        return clone;
    }

    public static int findIndex(int index, List<int[]> perm) {
        for (int i = perm.size() - 1; i >= 0; i--) {
            index = perm.get(i)[index];
        }
        return index;
    }

    public static int findIndex(int index, int[] cycle, List<int[]> perm) {
        int size = cycle.length;
        int output = 0;
        int[] base = multiplyCycleWithFormers(cycle, perm, size);
        for (int i = 0; i < size; i++) {
            if (base[i] == index) {
                output = i;
                break;
            }
        }
        return output;
    }

    public static int findIndex(int index, Permutation cycle, Permutation former) {
        int size = cycle.size();
        int output = 0;
        Permutation perm = cycle.multiply(former);
        System.out.println(
                "find index"
                        + " "
                        + index
                        + " "
                        + cycle.toCycleString()
                        + " "
                        + former.toCycleString());

        for (int i = 0; i < size; i++) {
            if (perm.get(i) == index) {
                output = i;
                break;
            }
        }
        return output;
    }

    public static int findIndex(int index, Permutation cycle) {
        int size = cycle.size();
        int output = 0;
        for (int i = 0; i < size; i++) {
            if (cycle.get(i) == index) {
                output = i;
                break;
            }
        }
        return output;
    }

    public static int[] multiplyCycleWithFormers(int[] cycle, List<int[]> perm, int size) {
        int[] base = new int[size];
        int entry = 0;
        for (int i = 0; i < size; i++) {
            entry = cycle[i];
            for (int j = perm.size() - 1; j >= 0; j--) {
                entry = perm.get(j)[entry];
            }
            base[i] = entry;
        }
        return base;
    }
    /**
     * public static boolean formerPermutationsCheck(int index, int y, int total, int[][] A,
     * ArrayList<Integer> partition, ArrayList<Integer> newPartition, ArrayList<Permutation>
     * formerPermutations, Permutation cycleTransposition, boolean formerBlocks) { boolean
     * check=true; for(Permutation formerPermutation: formerPermutations) { pWriter.println("former
     * perm bigger check"+" "+formerPermutation.toCycleString()); } for(Permutation
     * formerPermutation: formerPermutations) { pWriter.println("formerPerm"+"
     * "+formerPermutation.toCycleString()); Permutation canonicalPermutation =
     * idPermutation(total); Permutation testPermutation=
     * formerPermutation.multiply(cycleTransposition); pWriter.println("former.mul(cycle)"+"
     * "+testPermutation.toCycleString()); canonicalPermutation=getCanonicalPermutatiom(index, y,
     * total, A, partition, newPartition, cycleTransposition, formerPermutation);
     * pWriter.println("canonical perm"+" "+canonicalPermutation.toCycleString()); int[] array = new
     * int[6]; pWriter.println("index"+" "+index); pWriter.println("test.get index"+"
     * "+testPermutation.get(index)); array=
     * actArray(actArray(actArray(A[testPermutation.get(index)],cycleTransposition),formerPermutation),canonicalPermutation);
     * pWriter.println("cycle canonical and former actions"+" "+Arrays.toString(array));
     * pWriter.println("add perm to the table"); if(biggest) { if(descendingOrdercomparison(index,
     * newPartition,A[index], array)) { pWriter.println("descending");
     * if(canonicalPermutation.isIdentity()) { pWriter.println("identity");
     * if(descBlockwiseCheck2(index,y,A,newPartition)) { pWriter.println("identity");
     * addRepresentatives(index,idPermutation(6)); }else { pWriter.println("identity non desc
     * false"); check=false; break; } }else { pWriter.println("non id");
     * if(testPermutation.equals(formerPermutation.multiply(cycleTransposition.multiply(canonicalPermutation))))
     * { if(equalAuto) { pWriter.println("equal auto id"); addRepresentatives(index,
     * idPermutation(6)); }else { pWriter.println("equal auto false"); check=false; break; } }else {
     * pWriter.println("cycle"+" "+cycleTransposition.toCycleString());
     * pWriter.println("canonical"+" "+canonicalPermutation.toCycleString());
     * addRepresentatives(index, cycleTransposition.multiply(canonicalPermutation)); } } }else {
     * pWriter.println("desc comp false"); check=false; break; } }else { pWriter.println("there is
     * something bigger"); check=false; } } return check; }*
     */

    /**
     * Former block representatives: After the test within the block, we test the row with the
     * former blocks representatives.
     *
     * @param index int row index
     * @param y int first row index in block
     * @param A int[][] adjacency matrix
     * @param newPartition ArrayList<Integer> atom partition
     * @param perm Permutation
     * @return boolean
     */
    public static boolean formerBlocksRepresentatives(
            int index, int y, int[][] A, ArrayList<Integer> newPartition, Permutation perm) {
        boolean check = true;
        ArrayList<ArrayList<Permutation>> removeList = describeRemovalList(y);
        /**
         * If we break within the nested loop, need to put a label to the outer loop also to break.
         */
        outer:
        for (int i = (y - 1); i >= 0; i--) {

            /**
             * No need to multiply them all since we test line by line in the list of
             * representatives.
             */
            for (Permutation rep : representatives.get(i)) {
                Permutation test = rep.multiply(perm);
                if (!descBlockCheck(test, newPartition, index, y, A, test)) {
                    check = false;
                    break outer;
                } else {
                    // deneme if(!equalBlockCheck(test,newPartition,index,y,A,test)) {
                    if (!rep.isIdentity()) {
                        removeList.get(i).add(rep);
                    }
                    // }
                }
            }
        }
        if (check) {
            removeFormerPermutations(y, removeList);
        }
        return check;
    }

    /**
     * Grund Thesis 3.3.19. If the permutations of the former block keep the row in descending order
     * or equal, we can avoid that permutations and remove from the list.
     *
     * @param y int y index of the block
     * @param removeList ArrayList<ArrayList<Permutation>> Permutations to remove from the list
     */
    public static void removeFormerPermutations(
            int y, ArrayList<ArrayList<Permutation>> removeList) {
        ArrayList<Permutation> check = new ArrayList<Permutation>();
        for (int i = (y - 1); i >= 0; i--) {
            check = removeList.get(i);
            if (check.size() != 0) {
                representatives.get(i).removeAll(check);
            }
        }
    }

    /**
     * Checking whether the modified and original rows are equal or not.
     *
     * @param cycleTransposition Permutation - cycle transposition
     * @param partition atom partition
     * @param index row index
     * @param y beginning index of a block
     * @param A adjacency matrix
     * @param testPermutation Permutation
     * @return
     */
    public static int[] actArray(int[] strip, Permutation p) {
        int permLength = p.size();
        int arrayLength = strip.length;
        int[] modified = new int[arrayLength];
        for (int i = 0; i < permLength; i++) {
            modified[p.get(i)] = strip[i];
        }
        return modified;
    }

    public static int[] actCycleOnArray(int[] strip, int i, int j) {
        int perm = strip[i];
        strip[i] = strip[j];
        strip[j] = perm;
        return strip;
    }
    /**
     * Same as actArray just we use cycles.
     *
     * @param strip
     * @param p
     * @return
     */
    public static int[] cycleActions(int[] strip, int[] p) {
        int permLength = p.length;
        for (int i = 0; i < permLength; i++) {
            if (i != p[i] && i < p[i]) {
                strip = actCycleOnArray(strip, i, p[i]);
            }
        }
        return strip;
    }

    public static int[] formerCyclesActions(int[] strip, List<int[]> formerCycles) {
        /** The action of the formers should start form the last cycle. */
        int size = formerCycles.size() - 1;
        for (int i = size; i >= 0; i--) {
            strip = cycleActions(strip, formerCycles.get(i));
        }
        return strip;
    }

    /**
     * public static boolean equalBlockCheck(ArrayList<Integer> partition, int index, int y, int[][]
     * A, Permutation cycleTransposition,Permutation formerPermutation){
     * pWriter.println("equalBlockCheck"); boolean check=true; int[] canonical= A[index]; int[]
     * original= A[index]; Permutation mult= formerPermutation.multiply(cycleTransposition);
     * pWriter.println("former "+" "+formerPermutation.toCycleString()); pWriter.println("cycle"+"
     * "+cycleTransposition.toCycleString()); pWriter.println("former cycle"+"
     * "+mult.toCycleString()); pWriter.println("index"+" "+index); pWriter.println("mult index"+"
     * "+mult.get(index)); //original=actArray(A[cycleTransposition.get(index)],formerPermutation);
     * //original=actArray(actArray(A[cycleTransposition.get(index)],cycleTransposition),formerPermutation);
     * //original=actArray(actArray(actArray(A[cycleTransposition.get(index)],cycleTransposition),formerPermutation),perm);
     * original=actArray(actArray(A[mult.get(index)],cycleTransposition),formerPermutation);
     * pWriter.println("mul action ile"+" "+Arrays.toString(original));
     * if(!equalCheck(index,canonical, original, partition)) { check=false; }
     * /**if(!Arrays.equals(canonical,original)) { check=false; }*
     */
    // return check;
    // }

    public static boolean equalBlockCheck(
            ArrayList<Integer> partition,
            int index,
            int y,
            int[][] A,
            Permutation cycleTransposition,
            Permutation perm) {
        boolean check = true;
        int[] canonical = A[index];
        int[] original = A[index];
        // Permutation mult= formerPermutation.multiply(cycleTransposition);
        // pWriter.println("former "+" "+formerPermutation.toCycleString());
        // pWriter.println("former cycle"+" "+mult.toCycleString());
        // pWriter.println("mult index"+" "+mult.get(index));
        // original=actArray(A[cycleTransposition.get(index)],formerPermutation);
        // original=actArray(actArray(A[cycleTransposition.get(index)],cycleTransposition),formerPermutation);
        // original=actArray(actArray(actArray(A[cycleTransposition.get(index)],cycleTransposition),formerPermutation),perm);
        // original=actArray(actArray(A[mult.get(index)],cycleTransposition),formerPermutation);
        // yeni version former belirler index
        original =
                actArray(
                        actArray(A[cycleTransposition.get(index)], cycleTransposition),
                        perm); // siteden
        /** if(!equalCheck(index,canonical, original, partition)) { check=false; }* */
        if (!Arrays.equals(canonical, original)) {
            check = false;
        }
        return check;
    }

    public static boolean equalBlockCycleCheck(
            ArrayList<Integer> partition,
            int index,
            int y,
            int[][] A,
            int[] cycleTransposition,
            List<int[]> formers,
            int[] perm) {
        boolean check = true;
        int[] canonical = A[index];
        int[] array = row2compare(index, A, cycleTransposition, formers);
        if (!Arrays.equals(canonical, array)) {
            check = false;
        }
        return check;
    }

    public static boolean equalBlockCheck(
            ArrayList<Integer> partition,
            int index,
            int y,
            int[][] A,
            Permutation cycleTransposition,
            Permutation former,
            Permutation perm) {
        boolean check = true;
        int[] canonical = A[index];
        int[] array = row2compare(index, A, cycleTransposition, former);
        if (!Arrays.equals(canonical, array)) {
            check = false;
        }
        return check;
    }

    public static boolean equalBlockCycleCheck(
            ArrayList<Integer> partition,
            int index,
            int y,
            int[][] A,
            int[] cycleTransposition,
            int[] perm) {
        boolean check = true;
        int[] canonical = A[index];
        int[] array = row2compare(index, A, cycleTransposition);
        if (!Arrays.equals(canonical, array)) {
            check = false;
        }
        return check;
    }

    public static boolean equalCheck(int[] former, int[] current, ArrayList<Integer> partition) {
        boolean check = true;
        int i = 0;
        for (int k = 0; k < partition.size(); k++) {
            Integer[] can = getBlocks(former, i, partition.get(k) + i);
            Integer[] org = getBlocks(current, i, partition.get(k) + i);
            if (!Arrays.equals(can, org)) {
                check = false;
                break;
            } else {
                i = i + partition.get(k);
                continue;
            }
        }
        return check;
    }

    public static boolean formerIsBigger(
            int[][] A,
            int index,
            Permutation formerPermutation,
            Permutation cycleTransposition,
            Permutation canonicalPermutation,
            ArrayList<Integer> newPartition) {
        boolean check = true;
        int[] original = A[index];
        int[] permuted = A[index];
        // permuted=actArray(actArray(actArray(A[cycleTransposition.get(index)],cycleTransposition),canonicalPermutation),formerPermutation);
        Permutation bu = formerPermutation.multiply(cycleTransposition);
        // permuted=actArray(actArray(actArray(A[cycleTransposition.get(index)],cycleTransposition),formerPermutation),canonicalPermutation);
        permuted = actArray(actArray(A[bu.get(index)], bu), canonicalPermutation);
        // deneme check=descendingOrdercomparison(index,newPartition, original, permuted);
        return check;
    }

    /**
     * public static boolean descendingOrdercomparison(int index, ArrayList<Integer> partition,
     * int[] original, int[] permuted){ pWriter.println("descendingOrderComparison");
     * pWriter.println("partition"+" "+partition); pWriter.println("original"+"
     * "+Arrays.toString(original)); pWriter.println("permuted"+" "+Arrays.toString(permuted));
     * boolean check=true; int i=index+1; for(int k=index+1;k<partition.size();k++) { Integer[] org=
     * getBlocks(original,i,partition.get(k)+i); Integer[] perm=
     * getBlocks(permuted,i,partition.get(k)+i); if(desOrderCheck(org)) {
     * if(!Arrays.equals(org,perm)) { if(toInt(org)==toInt(perm)) { continue; }else
     * if(toInt(org)>toInt(perm)) { check=true; break; //If bigger than already in lexico order.
     * }else if(toInt(org)<toInt(perm)) { check=false; break; } } i=i+partition.get(k); }else {
     * check=false; break; } } /**for(Integer p:partition) { Integer[] org=
     * getBlocks(original,i,p+i); Integer[] perm= getBlocks(permuted,i,p+i); if(desOrderCheck(org))
     * { if(!Arrays.equals(org,perm)) { if(toInt(org)==toInt(perm)) { continue; }else
     * if(toInt(org)>toInt(perm)) { check=true; break; //If bigger than already in lexico order.
     * }else if(toInt(org)<toInt(perm)) { check=false; break; } } i=i+p; }else { check=false; break;
     * } }*
     */
    /** pWriter.println("descending order comparison"+" "+check); return check; }* */
    public static boolean descendingOrdercomparison(
            ArrayList<Integer> partition, int[] original, int[] permuted) {
        boolean check = true;
        int i = 0;
        for (Integer p : partition) {
            Integer[] org = getBlocks(original, i, p + i);
            Integer[] perm = getBlocks(permuted, i, p + i);
            if (desOrderCheck(org)) {
                if (!Arrays.equals(org, perm)) {
                    if (toInt(org) == toInt(perm)) {
                        continue;
                    } else if (toInt(org) > toInt(perm)) {
                        check = true;
                        break; // If bigger than already in lexico order.
                    } else if (toInt(org) < toInt(perm)) {
                        check = false;
                        break;
                    }
                }
                i = i + p;
            } else {
                check = false;
                break;
            }
        }
        return check;
    }

    public static boolean descendingOrderUpperMatrix(
            int index, ArrayList<Integer> partition, int[] original, int[] permuted) {
        boolean check = true;
        int i = index + 1;
        for (int k = index + 1; k < partition.size(); k++) {
            int p = partition.get(k);
            Integer[] org = getBlocks(original, i, p + i);
            Integer[] perm = getBlocks(permuted, i, p + i);
            if (desOrderCheck(org)) {
                if (!Arrays.equals(org, perm)) {
                    if (toInt(org) == toInt(perm)) {
                        continue;
                    } else if (toInt(org) > toInt(perm)) {
                        check = true;
                        break; // If bigger than already in lexico order.
                    } else if (toInt(org) < toInt(perm)) {
                        check = false;
                        break;
                    }
                }
                i = i + p;
            } else {
                check = false;
                break;
            }
        }
        return check;
    }

    public static boolean equalSetCheck(
            ArrayList<Integer> partition, int[] original, int[] permuted) {
        boolean check = true;
        int i = 0;
        for (Integer p : partition) {
            Integer[] org = getBlocks(original, i, p + i);
            Integer[] perm = getBlocks(permuted, i, p + i);
            if (Arrays.equals(org, perm)) {
                i = i + p;
            } else {
                check = false;
                break;
            }
        }
        return check;
    }

    public static boolean descendingOrderCheck(Integer[] original, Integer[] permuted) {
        boolean check = true;
        if (desOrderCheck(original)) {
            if (!Arrays.equals(original, permuted)) {
                if (toInt(original) > toInt(permuted)) {
                    check = true;
                } else if (toInt(original) < toInt(permuted)) {
                    check = false;
                }
            }
        } else {
            check = false;
        }
        return check;
    }
    /**
     * Describing ArrayList<ArrayList<Permutation>> for formerBlocksRepresentatives function.
     *
     * @param y int index of the first row in block
     * @return ArrayList<ArrayList<Permutation>> permutations to remove.
     */

    // TODO: Not complete !!!

    public static ArrayList<ArrayList<Permutation>> describeRemovalList(int y) {
        ArrayList<ArrayList<Permutation>> removeList = new ArrayList<ArrayList<Permutation>>();
        for (int i = 0; i < y; i++) {
            ArrayList<Permutation> list = new ArrayList<Permutation>();
            removeList.add(i, list);
        }
        return removeList;
    }

    public static Integer[] getBlocks(int[] row, int begin, int end) {
        return IntStream.range(begin, end).mapToObj(i -> row[i]).toArray(Integer[]::new);
    }

    /**
     * Descending order check for a integer array
     *
     * @param array int[]
     * @return boolean
     */
    public static boolean desOrderCheck(Integer[] array) {
        boolean check = true;
        int length = array.length;
        for (int i = 0; i < length - 1; i++) {
            if (array[i] < array[i + 1]) {
                check = false;
                break;
            }
        }
        return check;
    }

    /**
     * int array to int representation. This is needed for blockwise descending order check.
     *
     * @param array
     * @return
     */
    public static int toInt(Integer[] array) {
        int result = 0;
        for (int i = 0; i < array.length; i++) {
            result = result * 10;
            result = result + array[i];
        }
        return result;
    }

    /**
     * To check whether the block is in descending order after the permutation actions.
     *
     * @param cycle Permutation cycle
     * @param partition ArrayList<Integer> atom partition
     * @param index int row index
     * @param y int y index of the block
     * @param A int[][] adjacency matrix
     * @param perm Permutation
     * @return
     */
    public static boolean descBlockCheck(
            Permutation cycle,
            ArrayList<Integer> partition,
            int index,
            int y,
            int[][] A,
            Permutation perm) {
        boolean check = true;
        int[] canonical = A[index];
        int[] original = A[index];
        original = actArray(A[cycle.get(index)], cycle);
        int[] temp = new int[sum(partition)];
        if (cycle.get(index) < index) {
            temp = canonical;
            canonical = original;
            original = temp;
        }

        // int[] original=actArray(A[index],perm);

        int i = 0;
        for (Integer p : partition) {
            Integer[] can = getBlocks(canonical, i, p + i);
            Integer[] org = getBlocks(original, i, p + i);
            // if(desOrderCheck(can) && desOrderCheck(org)) { not both the first should be desc
            if (desOrderCheck(can)) {
                if (!Arrays.equals(can, org)) {
                    if (toInt(can) == toInt(org)) {
                        continue;
                    } else if (toInt(can) > toInt(org)) {
                        check = true;
                        break; // If bigger than already in lexico order.
                    } else if (toInt(can) < toInt(org)) {
                        check = false;
                        break;
                    }
                }
                /** else { if(!descendingOrderCheck(can)) { check=false; break; } }* */
                i = i + p;
            } else {
                check = false;
                break;
            }
        }
        return check;
    }

    /**
     * Within blockwise checking the row is descending or not after the permutation action.
     *
     * @param index int row index
     * @param y int y index of the block
     * @param A int[][] adjacency matrix
     * @param partition ArrayList<Integer> partition
     * @return
     */
    public static boolean descBlockwiseCheck2(
            int index, int y, int[][] A, ArrayList<Integer> partition) {
        int[] array = A[index];
        boolean check = true;
        // int i=0;
        // for(Integer p:partition) {
        int i = index + 1;
        for (int k = index + 1; k < partition.size(); k++) {
            Integer[] can = getBlocks(array, i, partition.get(k) + i);
            if (desOrderCheck(can)) {
                i = i + partition.get(k);
                continue;
            } else {
                check = false;
                break;
            }
        }
        return check;
    }

    public static boolean descendingOrderCheck(
            int index, int[] array, ArrayList<Integer> partition) {
        boolean check = true;
        int i = index + 1;
        for (int k = index + 1; k < partition.size(); k++) {
            Integer[] can = getBlocks(array, i, partition.get(k) + i);
            if (desOrderCheck(can)) {
                i = i + partition.get(k);
                continue;
            } else {
                check = false;
                break;
            }
        }
        return check;
    }

    public static ArrayList<Integer[]> getBlocks(
            int index, int[] array, ArrayList<Integer> partition) {
        // System.out.println("array"+" "+Arrays.toString(array));
        int i = index;
        ArrayList<Integer[]> list = new ArrayList<Integer[]>();
        for (int r = index + 1; r < partition.size(); r++) {
            // System.out.println("partition"+" "+partition.get(r));
            Integer[] can = getBlocks(array, i, partition.get(r) + i);
            list.add(can);
            i = i + partition.get(r);
        }
        return list;
    }

    public static int find1sEnd(ArrayList<Integer> partition) {
        int i = 0;
        for (int k = 0; k < partition.size(); k++) {
            if (partition.get(k) != 1) {
                i = i + k;
                break;
            }
        }
        return i;
    }

    public static boolean descBlockComp(int index, int[] array, ArrayList<Integer> partition) {
        // System.out.println("array"+" "+Arrays.toString(array));
        boolean check = true;
        ArrayList<Integer[]> blocks = getBlocks(index, array, partition);
        Integer[] max = blocks.get(0);
        for (int s = 0; s < blocks.size(); s++) {
            // System.out.println("block"+" "+s+" "+Arrays.toString(blocks.get(s)));
        }

        for (int i = 1; i < blocks.size(); i++) {
            if (!bigger(max, blocks.get(i))) {
                check = false;
                break;
            }
        }
        // System.out.println("des check"+" "+check);
        return check;
    }

    public static boolean allIs0(Integer[] array) {
        boolean check = true;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != 0) {
                check = false;
                break;
            }
        }
        return check;
    }

    public static boolean zeros = false;

    public static boolean descBlockLineCheck(
            int index, int[] former, int[] current, ArrayList<Integer> partition) {
        boolean check = true;
        int i = index + 1;
        for (int k = index + 1; k < partition.size(); k++) {
            Integer[] can = getBlocks(former, i, partition.get(k) + i);
            Integer[] org = getBlocks(current, i, partition.get(k) + i);
            if (!Arrays.equals(can, org)) {
                if (toInt(can) == toInt(org)) {
                    continue;
                } else if (toInt(can) > toInt(org)) {
                    check = true;
                    break; // If bigger than already in lexico order.
                } else if (toInt(can) < toInt(org)) {
                    check = false;
                    break;
                }
            }
            /**
             * if(k==partition.size()-1) { if(allIs0(can)) { zeros=true; continue; }else {
             * if(!Arrays.equals(can,org)) { if(toInt(can)==toInt(org)) { continue; }else
             * if(toInt(can)>toInt(org)) { check=true; break; //If bigger than already in lexico
             * order. }else if(toInt(can)<toInt(org)) { check=false; break; } } } }else {
             * if(!Arrays.equals(can,org)) { if(toInt(can)==toInt(org)) { continue; }else
             * if(toInt(can)>toInt(org)) { check=true; break; //If bigger than already in lexico
             * order. }else if(toInt(can)<toInt(org)) { check=false; break; } } }*
             */
            // }
            i = i + partition.get(k);
        }
        return check;
    }

    public static boolean bigger(Integer[] arr, Integer[] arr2) {
        boolean check = true;
        int size = getSmaller(arr, arr2);
        for (int i = 0; i < size; i++) {
            if (arr[i] < arr2[i]) {
                check = false;
                break;
            }
        }
        return check;
    }

    public static int getSmaller(Integer[] arr, Integer[] arr2) {
        int size = 0;
        size = arr.length;
        if (arr2.length < size) {
            size = arr2.length;
        }
        return size;
    }

    /**
     * public static Permutation getCanonicalPermutation(int index, int y, int total, int[][] A,
     * ArrayList<Integer> partition, ArrayList<Integer> newPartition, Permutation cycleM,
     * PrintWriter pWriter) { pWriter.print("getCanonicalPermutation"+"\n");
     * pWriter.print("permutation"+" "+cycleM+"\n"); //PermutationGroup group=
     * getYoungGroup(newPartition,total); Permutation canonical = idPermutation(total);
     * pWriter.print("des"+" "+descBlockCheck(cycleM,newPartition,index,y,A,idPermutation(total))+"
     * "+"\n"); pWriter.print("equal"+"
     * "+equalBlockCheck(cycleM,newPartition,index,y,A,idPermutation(total), pWriter)+" "+"\n");
     * if(descBlockCheck(cycleM,newPartition,index,y,A,idPermutation(total))){
     * if(!equalBlockCheck(cycleM,newPartition,index,y,A,idPermutation(total), pWriter)) {
     * canonical=getEqualPermutation(cycleM, index, y, total,A, partition, newPartition, pWriter);
     * pWriter.print("equal perm"+" "+canonical.toCycleString()+"\n");
     * canonical=cycleM.multiply(canonical); }else { /** If id then not canonical but if cycle then
     * no need to add anything to reps and ignore that cycle for the further steps.
     */
    // canonical=cycleM.multiply(canonical);
    // }
    // }else {

    /** des already check equal or descending. */

    /**
     * canonical=getEqualPermutation(cycleM, index, y, total, A, partition, newPartition, pWriter);
     * if(!canonical.isIdentity()) { canonical=cycleM.multiply(canonical); }*
     */
    // }
    // return canonical;
    // }

    /**
     * Checks whether there is an automorphism permutation mapping the row itself and keeping it in
     * maximal form.
     *
     * @param cycle Permutation cycle
     * @param index int row index
     * @param y int y index of the block
     * @param total int number of atoms
     * @param A int[][] adjacency matrix
     * @param partition ArrayList<Integer> atom partition
     * @param newPartition ArrayList<Integer> new partition after canonical re-partitioning
     * @return
     */
    public static boolean formerPermutationsCheck = true;

    public static Permutation getEqualPermutation(
            Permutation cycleTransposition,
            int index,
            int y,
            int total,
            int[][] A,
            ArrayList<Integer> partition,
            ArrayList<Integer> newPartition) {
        int size = sum(newPartition);
        // PermutationGroup group= getYoungGroup(newPartition,total);
        // original=actArray(actArray(actArray(A[cycleTransposition.get(index)],cycleTransposition),formerPermutation),perm);
        // Permutation mult= formerPermutation.multiply(cycleTransposition); bu former olunca
        // int[] check=actArray(actArray(A[mult.get(index)],cycleTransposition),formerPermutation);
        int[] check = actArray(A[cycleTransposition.get(index)], cycleTransposition);
        Permutation canonicalPermutation = new Permutation(size);

        // deneme canonicalPermutation= getCyclePermutation(A[index],check,newPartition); //int
        // deisti normalde orada
        // int[] perm= actArray(check,canonicalPermutation); we did not have that before
        // if(!biggerCheck(index, A[index],perm,newPartition)) { // burada check vard perm deil
        // deneme if(!biggerCheck(A[index],check,newPartition)) {
        biggest = false;
        // }
        /**
         * for(Permutation permutation : group.all()) { if(!permutation.isIdentity()) {
         * if(equalBlockCheck(newPartition,index,y,A,formerPermutation, cycleTransposition,
         * permutation)){ canonicalPermutation = permutation; if(!formerIsBigger(A, index,
         * formerPermutation, cycleTransposition, canonicalPermutation, newPartition)) {
         * formerPermutationsCheck=false; } break; }else { if(!formerIsBigger(A, index,
         * formerPermutation, cycleTransposition, idPermutation(6), newPartition)) {
         * formerPermutationsCheck=false; } }
         *
         * <p>/**if(equalBlockCheck(cycleTransposition,newPartition,index,y,A,formerPermutation.multiply(permutation))){
         * canonicalPermutation = permutation; break; }*
         */
        // }
        // }
        return canonicalPermutation;
    }

    public static int[] getEqualPermCycle(
            List<int[]> formers,
            int[] cycleTransposition,
            int index,
            int y,
            int total,
            int[][] A,
            ArrayList<Integer> partition,
            ArrayList<Integer> newPartition) {
        /**
         * int[] check = A[findIndex(cycleTransposition[index],formers)];
         * check=formerCyclesActions(cycleActions(check,cycleTransposition), formers);*
         */
        int[] check = row2compare(index, A, cycleTransposition, formers);
        int[] canonicalPermutation = getCyclePermutation(A[index], check, newPartition);
        if (!biggerCheck(index, A[index], check, newPartition)) {
            biggest = false;
        }
        return canonicalPermutation;
    }

    public static int[] getEqualPermCycle(
            int[] cycleTransposition,
            int index,
            int y,
            int total,
            int[][] A,
            ArrayList<Integer> partition,
            ArrayList<Integer> newPartition) {
        int[] check = row2compare(index, A, cycleTransposition);
        int[] canonicalPermutation = getCyclePermutation(A[index], check, newPartition);
        if (!biggerCheck(index, A[index], check, newPartition)) {
            biggest = false;
        }
        return canonicalPermutation;
    }

    public static Permutation getEqualPerm(
            Permutation cycleTransposition,
            int index,
            int y,
            int total,
            int[][] A,
            ArrayList<Integer> partition,
            ArrayList<Integer> newPartition) {
        int[] check = row2compare(index, A, cycleTransposition);
        Permutation canonicalPermutation = getCanonicalPermutation(A[index], check, newPartition);
        // int[] canonicalPermutation= getCyclePermutation(A[index],check,newPartition);
        // Permutation permutation = new Permutation(canonicalPermutation);
        if (!biggerCheck(index, A[index], check, newPartition)) {
            biggest = false;
        }
        return canonicalPermutation;
    }
    /**
     * public static Permutation getEqualPermutation(Permutation cycleTransposition, Permutation
     * formerPermutation, int index, int y, int total, int[][] A, ArrayList<Integer> partition,
     * ArrayList<Integer> newPartition) { int size=sum(newPartition); //PermutationGroup group=
     * getYoungGroup(newPartition,total);
     * //original=actArray(actArray(actArray(A[cycleTransposition.get(index)],cycleTransposition),formerPermutation),perm);
     * pWriter.println("getEqualPerm"); pWriter.println("A[index]"+" "+Arrays.toString(A[index]));
     * pWriter.println("former"+" "+formerPermutation.toCycleString()); pWriter.println("cycle"+"
     * "+cycleTransposition.toCycleString()); Permutation mult=
     * formerPermutation.multiply(cycleTransposition); pWriter.println("former mult cycle"+"
     * "+mult.toCycleString()); int[]
     * check=actArray(actArray(A[mult.get(index)],cycleTransposition),formerPermutation);
     * pWriter.println("A[mult(index)] mult action"+" "+Arrays.toString(check)); Permutation
     * canonicalPermutation= new Permutation(size); canonicalPermutation=
     * getCyclePermutation(A[index],check,newPartition); pWriter.println("canonicalPermutation
     * getCyclePermutation"+" "+canonicalPermutation.toCycleString()); int[] perm=
     * actArray(check,canonicalPermutation); pWriter.println("after perm action"+"
     * "+Arrays.toString(perm)); if(!biggerCheck(index, A[index],perm,newPartition)) { // burada
     * check vard perm deil pWriter.println("bigger is false"); biggest=false; } /**for(Permutation
     * permutation : group.all()) { if(!permutation.isIdentity()) {
     * if(equalBlockCheck(newPartition,index,y,A,formerPermutation, cycleTransposition,
     * permutation)){ canonicalPermutation = permutation; if(!formerIsBigger(A, index,
     * formerPermutation, cycleTransposition, canonicalPermutation, newPartition)) {
     * formerPermutationsCheck=false; } break; }else { if(!formerIsBigger(A, index,
     * formerPermutation, cycleTransposition, idPermutation(6), newPartition)) {
     * formerPermutationsCheck=false; } }
     *
     * <p>/**if(equalBlockCheck(cycleTransposition,newPartition,index,y,A,formerPermutation.multiply(permutation))){
     * canonicalPermutation = permutation; break; }*
     */
    // }
    // }
    // return canonicalPermutation;
    // }

    public static boolean testBig(
            Permutation cycleTransposition,
            Permutation testPermutation,
            int index,
            int total,
            int[][] A,
            ArrayList<Integer> newPartition) {
        boolean check = true;
        PermutationGroup group = getYoungGroup(newPartition, total);
        for (Permutation permutation : group.all()) {
            int[] array = actArray(A[cycleTransposition.get(index)], cycleTransposition);
            // if(!descendingOrdercomparison(newPartition, A[index],
            // actArray(A[cycleTransposition.get(index)],testPermutation.multiply(permutation)))) {
            if (!descendingOrdercomparison(
                    newPartition,
                    A[index],
                    actArray(array, testPermutation.multiply(permutation)))) {
                check = false;
                break;
            }
        }
        return check;
    }

    public static ArrayList<ArrayList<Integer>> subPartitionsList(ArrayList<Integer> partition) {
        ArrayList<ArrayList<Integer>> parts = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < partition.size(); i++) {
            parts.add(subPartition(partition, i));
        }
        return parts;
    }

    /**
     * To get the canonical partition like in Grund Thesis 3.3.11
     *
     * @param i int row index
     * @param partition ArrayList<Integer> partition
     * @return
     */
    public static ArrayList<Integer> canonicalPartition(int i, ArrayList<Integer> partition) {
        return partitionCriteria(partition, i + 1);
        /**
         * if(i==0) { return partitionCriteria(partition,1); }else { return
         * partitionCriteria(partition,i+1); }*
         */
    }

    /**
     * Grund Thesis 3.3.2 Partitioning criteria (DONE)
     *
     * @param partEx the former partition
     * @param degree degree of the partitioning.
     * @return
     */
    public static ArrayList<Integer> partitionCriteria(ArrayList<Integer> partEx, int degree) {
        ArrayList<Integer> partNew = new ArrayList<Integer>();
        partNew = addOnes(partNew, degree);
        /** I had (degree-1) */
        if (partEx.get(degree - 1) > 1) {
            partNew.add(partEx.get(degree - 1) - 1);
            for (int k = degree; k < partEx.size(); k++) {
                partNew.add(partEx.get(k));
            }
        } else if (partEx.get(degree - 1) == 1) {
            for (int k = degree; k < partEx.size(); k++) {
                partNew.add(partEx.get(k));
            }
        }
        return partNew;
    }

    /** add number of 1s into a ArrayList */
    public static ArrayList<Integer> addOnes(ArrayList<Integer> list, int number) {
        for (int i = 0; i < number; i++) {
            list.add(1);
        }
        return list;
    }

    /** 3.6.2. Connectivity Test */

    /**
     * Finding the N values as explained in 3.6.2.
     *
     * @param index int row index
     * @param total int number of atoms.
     * @param mat int[][] adjacency matrix
     * @return Set<Integer>
     */
    public static Set<Integer> nValues(int index, int total, int[][] mat) {
        Set<Integer> nValues = new HashSet<Integer>();
        nValues.add(index);
        int[] theRow = mat[index];
        for (int i = (index + 1); i < total; i++) {
            if (theRow[i] > 0) {
                nValues.add(i);
            }
        }
        return nValues;
    }

    /**
     * Finding the W values as explained in 3.6.2.
     *
     * @param nValues ArrayList<Integer> N values
     * @param Kformer ArrayList<Integer> the K values of the former step
     * @return Set<Integer>
     */
    public static Set<Integer> wValues(Set<Integer> nValues, int[] Kformer) {
        Set<Integer> wValues = new HashSet<Integer>();
        for (Integer i : nValues) {
            wValues.add(Kformer[i]);
        }
        return wValues;
    }

    /**
     * Finding the K values as explained in 3.6.2.
     *
     * @param wValues Set<Integer> wValues
     * @param kFormer ArrayList<Integer> the K values of the former step
     * @return ArrayList<Integer>
     */
    public static int[] kValues(int total, Set<Integer> wValues, int[] kFormer) {
        int[] kValues = new int[total];
        int min = Collections.min(wValues);
        for (int i = 0; i < total; i++) {
            if (wValues.contains(kFormer[i])) {
                kValues[i] = min;
            } else {
                kValues[i] = kFormer[i];
            }
        }
        return kValues;
    }

    /**
     * Test whether an adjacency matrix is connected or disconnected.
     *
     * @param p int the index where the atoms with degree 1 starts.
     * @param mat int[][] adjacency matrix
     * @return boolean
     */
    public static boolean connectivityTest(int p, int[][] mat) {
        boolean check = false;
        int total = mat.length;
        int[] kValues = initialKList(total);
        Set<Integer> nValues = new HashSet<Integer>();
        Set<Integer> wValues = new HashSet<Integer>();
        int zValue = 0;
        for (int i = 0; i < p; i++) {
            nValues = nValues(i, total, mat);
            wValues = wValues(nValues, kValues);
            zValue = Collections.min(wValues);
            kValues = kValues(total, wValues, kValues);
        }
        if (zValue == 0 && allIs0(kValues)) {
            check = true;
        }
        return check;
    }

    /**
     * Checks whether all the entries are equal to 0 or not. (Grund 3.6.2)
     *
     * @param list ArrayList<Integer>
     * @return boolean
     */
    public static boolean allIs0(int[] list) {
        boolean check = true;
        for (int i = 0; i < list.length; i++) {
            if (list[i] != 0) {
                check = false;
                break;
            }
        }
        return check;
    }

    /**
     * Initializing the first K values list.
     *
     * @param total int number of atoms.
     * @return ArrayList<Integer>
     */
    public static int[] initialKList(int total) {
        int[] k = new int[total];
        for (int i = 0; i < total; i++) {
            k[i] = i;
        }
        return k;
    }

    /**
     * The initializer function, reading the formula to set the degrees, partition and file
     * directory variables.
     *
     * @param formula String molecular formula
     * @param filedir String file directory
     * @throws IOException
     * @throws CDKException
     * @throws CloneNotSupportedException
     */

    /**
     * Setting the symbols and occurrences global variables.
     *
     * @param formula String molecular formula
     */
    public static void getSymbolsOccurrences(String formula) {
        String[] atoms = formula.split("(?=[A-Z])");
        for (String atom : atoms) {
            String[] info = atom.split("(?=[0-9])", 2);
            // if(!info[0].contains("H")) {
            symbols.add(info[0]);
            occurrences.add(atomOccurrunce(info));
            // }
        }
    }

    /**
     * Degree sequence is set from formula
     *
     * @param formula String molecular formula
     * @return int[] valences
     */
    public static void initialDegrees() {
        int size = sum(occurrences);
        initialDegrees = new int[size];
        int index = 0;
        for (int i = 0; i < symbols.size(); i++) {
            String symbol = symbols.get(i);
            for (int j = 0; j < occurrences.get(i); j++) {
                initialDegrees[index] = valences.get(symbol);
                index++;
            }
        }
    }

    /**
     * deneme
     *
     * <p>public static void initialPartition() { int size=occurrences.size(); initialPartition= new
     * int[size]; for(int i=0;i<size;i++) { initialPartition[i]=occurrences.get(i); } }*
     */
    public static int atomOccurrunce(String[] info) {
        int number = 1;
        if (info.length > 1) {
            number = Integer.parseInt(info[1]);
        }
        return number;
    }

    /**
     * Degree sequence is set from formula
     *
     * @param formula String
     * @return
     */
    public static ArrayList<Integer> distribution(String formula) {
        List<String> symbols = new ArrayList<String>();
        ArrayList<Integer> occur = new ArrayList<Integer>();
        String[] atoms = formula.split("(?=[A-Z])");
        for (String atom : atoms) {
            String[] info = atom.split("(?=[0-9])", 2);
            symbols.add(info[0]);
            occur.add(info.length > 1 ? Integer.parseInt(info[1]) : 1);
        }
        return occur;
    }

    public static ArrayList<String> getSymbolList(String formula) {
        ArrayList<String> symbolsList = new ArrayList<String>();
        String[] atoms = formula.split("(?=[A-Z])");
        Integer occur;
        for (String atom : atoms) {
            String[] info = atom.split("(?=[0-9])", 2);
            occur = (info.length > 1 ? Integer.parseInt(info[1]) : 1);
            for (int i = 0; i < occur; i++) {
                symbolsList.add(info[0]);
            }
        }
        return symbolsList;
    }

    public static ArrayList<Integer> getValenceList(String formula) {
        ArrayList<Integer> valenceList = new ArrayList<Integer>();
        String[] atoms = formula.split("(?=[A-Z])");
        Integer occur;
        for (String atom : atoms) {
            String[] info = atom.split("(?=[0-9])", 2);
            occur = (info.length > 1 ? Integer.parseInt(info[1]) : 1);
            for (int i = 0; i < occur; i++) {
                valenceList.add(valences.get(info[0]));
            }
        }
        return valenceList;
    }

    public static ArrayList<Integer> firstValences;
    public static ArrayList<String> firstSymbols;
    public static ArrayList<Integer> firstPartition;

    public static int hydrogenIndex;
    public static ArrayList<ArrayList<String>> inputSymbolsList =
            new ArrayList<ArrayList<String>>();
    public static ArrayList<ArrayList<Integer>> inputPartitionsList =
            new ArrayList<ArrayList<Integer>>();
    public static ArrayList<ArrayList<Integer>> inputValencesList =
            new ArrayList<ArrayList<Integer>>();
    public static ArrayList<String> newSymbols;

    public static void getOrderedSymbolsValences(String formula)
            throws FileNotFoundException, UnsupportedEncodingException, CloneNotSupportedException,
                    CDKException {
        ArrayList<Integer> valences = getValenceList(formula);
        ArrayList<String> symbols = getSymbolList(formula);
        sortSymbolsValences(valences, symbols);
        firstValences = valences;
        firstSymbols = symbols;
        firstPartition = getPartition(valences);
        // deneme inputLists(firstValences, firstPartition);
    }

    public static ArrayList<Integer> getPartition(ArrayList<Integer> valences) {
        ArrayList<Integer> partition = new ArrayList<Integer>();
        int count = 1;
        int size = valences.size();
        for (int k = 0; k < size - 1; k++) {
            if (valences.get(k) != valences.get(k + 1)) {
                partition.add(count);
                count = 1;
                if (k == size - 2) {
                    partition.add(count);
                }
            } else {
                count++;
                if (k == size - 2) {
                    partition.add(count);
                }
            }
        }
        return partition;
    }

    public static ArrayList<Integer> getPartitionSymbols(ArrayList<String> symbols) {
        ArrayList<Integer> partition = new ArrayList<Integer>();
        int count = 1;
        int size = symbols.size();
        for (int k = 0; k < size - 1; k++) {
            if (!symbols.get(k).equals(symbols.get(k + 1))) {
                partition.add(count);
                count = 1;
                if (k == size - 2) {
                    partition.add(count);
                }
            } else {
                count++;
                if (k == size - 2) {
                    partition.add(count);
                }
            }
        }
        return partition;
    }

    public static ArrayList<Integer> refinePartition(
            ArrayList<Integer> partition, ArrayList<String> symbols) {
        ArrayList<Integer> refined = new ArrayList<Integer>();
        int index = 0;
        int count = 1;
        for (Integer p : partition) {
            if (p != 1) {
                for (int i = index; i < p + index - 1; i++) {
                    if (i + 1 < p + index - 1) {
                        if (symbols.get(i).equals(symbols.get(i + 1))) {
                            count++;
                        } else {
                            refined.add(count);
                            count = 1;
                        }
                    } else {
                        if (symbols.get(i).equals(symbols.get(i + 1))) {
                            count++;
                            refined.add(count);
                            count = 1;
                        } else {
                            refined.add(count);
                            refined.add(1);
                            count = 1;
                        }
                    }
                }
                index = index + p;
            } else {
                index++;
                refined.add(1);
                count = 1;
            }
        }
        return refined;
    }

    /**
     * deneme
     *
     * <p>public static List<int[]> getHydrogenDistribution(ArrayList<Integer> valences,
     * ArrayList<Integer> partition) throws FileNotFoundException, UnsupportedEncodingException,
     * CloneNotSupportedException, CDKException { List<int[]> list=
     * HydrogenDistributor.run(partition,valences); hydrogenIndex=list.get(0).length; return list;
     * }*
     */

    /**
     * deneme
     *
     * <p>public static void inputLists(ArrayList<Integer> valences, ArrayList<Integer> partition)
     * throws FileNotFoundException, UnsupportedEncodingException, CloneNotSupportedException,
     * CDKException { List<int[]> distributions= getHydrogenDistribution(valences, partition);
     * newSymbols= getNewSymbols(firstSymbols,hydrogenIndex); for(int[] dist: distributions) {
     * ArrayList<String> symbols= new ArrayList<String>(newSymbols); ArrayList<Integer> newValences=
     * new ArrayList<Integer>(); for(int i=0;i<dist.length;i++) {
     * newValences.add(i,(valences.get(i)-dist[i])); } sortSymbolsValences(newValences, symbols);
     * begins.clear(); ends.clear(); findRepetetiveSubArrays(newValences);
     * inputValencesList.add(newValences); inputSymbolsList.add(sortSymbols(symbols));
     * inputPartitionsList.add(refinePartition(getPartition(newValences),sortSymbols(symbols))); }
     * }*
     */
    public static ArrayList<String> getNewSymbols(ArrayList<String> list, int size) {
        ArrayList<String> newSymbols = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            newSymbols.add(list.get(i));
        }
        return newSymbols;
    }

    public static List<Integer> begins = new ArrayList<Integer>();
    public static List<Integer> ends = new ArrayList<Integer>();

    public static void findRepetetiveSubArrays(ArrayList<Integer> list) {
        int begin = 0;

        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) == list.get(i + 1)) {
                if (begin == 0) {
                    begins.add(i);
                    begin++;
                }
                if ((i + 1) == (list.size() - 1)) {
                    ends.add(i + 1);
                }
            } else {
                if (begins.size() - 1 == ends.size()) {
                    begin = 0;
                    ends.add(i);
                }
            }
        }
    }

    public static final Comparator<Entry<String, Integer>> map_ORDER =
            new Comparator<Entry<String, Integer>>() {
                public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
                    return -e2.getValue().compareTo(e1.getValue());
                }
            };

    public static Map<String, Integer> countSymbols(ArrayList<String> list, int begin, int end) {
        Map<String, Integer> count = new LinkedHashMap<>();
        String sym;

        for (int i = begin; i <= end; i++) {
            sym = list.get(i);
            if (count.containsKey(sym)) {
                count.put(sym, count.get(sym) + 1);
            } else {
                count.put(sym, 1);
            }
        }
        return count;
    }

    public static ArrayList<String> sortString(ArrayList<String> list, int begin, int end) {
        ArrayList<String> output = new ArrayList<String>(list);
        Map<String, Integer> count = countSymbols(output, begin, end);
        ArrayList<Entry<String, Integer>> entryList = new ArrayList<>(count.entrySet());
        Collections.sort(entryList, map_ORDER);
        while (begin <= end) {
            for (Entry<String, Integer> entry : entryList) {
                int frequency = entry.getValue();
                while (frequency >= 1) {
                    output.set(begin, entry.getKey());
                    begin++;
                    frequency--;
                }
            }
        }
        return output;
    }

    public static ArrayList<String> sortSymbols(ArrayList<String> symbols) {
        ArrayList<String> newList = new ArrayList<String>(symbols);
        for (int i = 0; i < begins.size(); i++) {
            newList = sortString(newList, begins.get(i), ends.get(i));
        }
        return newList;
    }

    public static void sortSymbolsValences(ArrayList<Integer> valences, ArrayList<String> symbols) {
        int size = valences.size();
        int temp;
        String temp2;
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (valences.get(i) < valences.get(j)) {
                    temp2 = symbols.get(i);
                    temp = valences.get(i);
                    valences.set(i, valences.get(j));
                    symbols.set(i, symbols.get(j));
                    valences.set(j, temp);
                    symbols.set(j, temp2);
                }
            }
        }
    }

    private void parseArgs(String[] args) throws ParseException, IOException {
        Options options = setupOptions(args);
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            canonicalBlockwiseTest.formula = cmd.getOptionValue("formula");
            canonicalBlockwiseTest.filedir = cmd.getOptionValue("filedir");
            // setFileWriter();

            if (cmd.hasOption("verbose")) canonicalBlockwiseTest.verbose = true;
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.setOptionComparator(null);
            String header =
                    "\nGenerates 	molecular structures for a given molecular formula."
                            + " The input is a molecular formula string."
                            + "For example 'C2OH4'."
                            + "For this version, the hydrogens should be kept at the end of the string."
                            + "Besides this formula, the directory is needed to be specified for the output"
                            + "file. \n\n";
            String footer =
                    "\nPlease report issues at https://github.com/MehmetAzizYirik/AlgorithmicGroupTheory";
            formatter.printHelp(
                    "java -jar AlgorithmicGroupTheory.jar", header, options, footer, true);
            throw new ParseException("Problem parsing command line");
        }
    }

    private Options setupOptions(String[] args) {
        Options options = new Options();
        Option formula =
                Option.builder("f")
                        .required(true)
                        .hasArg()
                        .longOpt("formula")
                        .desc("formula (required)")
                        .build();
        options.addOption(formula);
        Option verbose =
                Option.builder("v")
                        .required(false)
                        .longOpt("verbose")
                        .desc("print message")
                        .build();
        options.addOption(verbose);
        Option filedir =
                Option.builder("d")
                        .required(true)
                        .hasArg()
                        .longOpt("filedir")
                        .desc("Creates and store the output txt file in the directory (required)")
                        .build();
        options.addOption(filedir);
        return options;
    }

    /**
     * ************************************************************************** *These are the
     * demo functions for the candidate matrix generation step.***
     * **************************************************************************
     */
    public static void run(String molecularFormula, String filePath)
            throws IOException, CDKException, CloneNotSupportedException {
        long startTime = System.nanoTime();
        formula = molecularFormula;
        filedir = filePath;
        getSymbolsOccurrences(formula);
        initialDegrees();
        ArrayList<Integer> part = new ArrayList<Integer>();
        part.add(6);
        initialPartition = part;
        build(formula);
        outFile = new SDFWriter(new FileWriter(filedir + "output.sdf"));
        canonicalBlockbasedGeneratorDemo();
        // if(verbose) System.out.println("The number of structures is: "+count);
        System.out.println("The number of structures is: " + count);
        outFile.close();
        long endTime = System.nanoTime() - startTime;
        double seconds = (double) endTime / 1000000000.0;
        DecimalFormat d = new DecimalFormat(".###");
        System.out.println("time" + " " + d.format(seconds));
    }

    /**
     * The main function for the block based canonical matrix generation.
     *
     * @param molecularFormula String molecular formula
     * @param degrees ArrayList<Integer> initial degree set.
     * @param newDegrees ArrayList<Integer> new degree set after hydrogen partition.
     * @param numberOfAtoms int total number of atoms.
     * @param hydrogenIndex int the beginning index of hydrogen atoms in A.
     * @param filePath String path to store the output files.
     * @throws IOException
     * @throws CloneNotSupportedException
     * @throws CDKException
     */
    public static void canonicalBlockbasedGeneratorDemo()
            throws IOException, CloneNotSupportedException, CDKException {
        size = initialDegrees.length;
        // List<int[]> newDegrees= distributeHydrogens(initialPartition, initialDegrees);
        hIndex = 6;
        partitionList.add(0, initialPartition);
        int[] newDegrees = new int[6];
        newDegrees[0] = 3;
        newDegrees[1] = 3;
        newDegrees[2] = 3;
        newDegrees[3] = 3;
        newDegrees[4] = 3;
        newDegrees[5] = 3;
        genStrip(newDegrees);
        /** for(int[] degree: newDegrees) { genDemo(degree); }* */
    }

    /**
     * The first step in Grund 3.2.3.
     *
     * <p>Initialization of the input variables for the generator.
     *
     * @param degreeList
     * @throws IOException
     * @throws CloneNotSupportedException
     * @throws CDKException
     */
    public static void genDemo(int[] degreeList)
            throws IOException, CloneNotSupportedException, CDKException {
        int[][] A = new int[size][size];
        degrees = degreeList;
        flag = true;
        maximalMatrix();
        upperTriangularL();
        upperTriangularC();
        int[] indices = new int[2];
        indices[0] = 0;
        indices[1] = 1;
        boolean callForward = true;

        while (flag) {
            int[][] mat = nextStep(A, indices, callForward);
            indices = successor(indices, max.length);
            callForward = true;
            nextStep(mat, indices, callForward);
        }
        // forwardDemo(A,indices,callForward);
    }

    public static int r = 0;
    public static int y = 0;
    public static int z = 0;

    public static void genStrip(int[] degreeList)
            throws IOException, CloneNotSupportedException, CDKException {
        int[][] A = new int[size][size];
        degrees = degreeList;
        flag = true;
        maximalMatrix();
        upperTriangularL();
        upperTriangularC();
        int[] indices = new int[2];
        indices[0] = 0;
        indices[1] = 1;
        boolean callForward = true;
        r = 0;
        y = findY(r);
        z = findZ(r);
        while (flag) {
            int[][] mat = nextStep(A, indices, callForward);
            indices = successor(indices, max.length);
            callForward = true;
            nextStep(mat, indices, callForward);
        }
        // forwardDemo(A,indices,callForward);
    }

    public static boolean stripIterate = true;
    public static boolean entryChanges = false;

    public static boolean canonicalTest(int[][] matrix)
            throws IOException, CloneNotSupportedException, CDKException {
        formerPermutationsCheck = true;
        // clear(false,findY(r));
        if (blockTest(r, matrix)) {
            // r++;
            return true;
            /** if(r==(initialPartition.size()-1)) { return false; }else { r++; return true; }* */
        } else {
            return false;
        }
    }

    public static boolean runStripGenerator = true;

    public static void noMoreFilling(int[][] matrix, boolean callForward)
            throws IOException, CloneNotSupportedException, CDKException {
        r--;
        if (r == (-1)) {
            runStripGenerator = false;
            // return matrix;
        }
        /** else { callForward=false; return stripGenerator(matrix,callForward); }* */
    }

    /**
     * The second step in Grund 3.2.3.
     *
     * <p>Forward step in filling matrix entry {i,j} in a maximal way.
     *
     * @param A int[][] adjacency matrix
     * @param indices ArrayList<Integer> indices
     * @throws IOException
     * @throws CloneNotSupportedException
     * @throws CDKException
     */
    public static int[][] forwardDemo(int[][] A, int[] indices, boolean callForward)
            throws IOException, CloneNotSupportedException, CDKException {
        int i = indices[0];
        int j = indices[1];
        int lInverse = LInverse(degrees, i, j, A);
        int cInverse = CInverse(degrees, i, j, A);
        int minimal = Math.min(max[i][j], Math.min(lInverse, cInverse));
        /** First step in the forward method. */
        int maximumValue = forwardMaximal(minimal, lInverse, L[i][j], cInverse, C[i][j]);
        callForward = true;
        return forwardSub(lInverse, cInverse, maximumValue, i, j, A, indices, callForward);
    }

    public static int[][] forwardRow(int[][] A, int[] indices, boolean callForward)
            throws IOException, CloneNotSupportedException, CDKException {
        int i = indices[0];
        int j = indices[1];
        int lInverse = LInverse(degrees, i, j, A);
        int cInverse = CInverse(degrees, i, j, A);
        int minimal = Math.min(max[i][j], Math.min(lInverse, cInverse));

        /** First step in the forward method. */
        int maximumValue = forwardMaximal(minimal, lInverse, L[i][j], cInverse, C[i][j]);
        callForward = true;
        if (j == (max.length - 1)) {
            int[][] newMat =
                    forwardSubRow(lInverse, cInverse, maximumValue, i, j, A, indices, callForward);
            return newMat;
        } else {
            return forwardSubRow(lInverse, cInverse, maximumValue, i, j, A, indices, callForward);
        }
    }

    /**
     * First step in the forward method.
     *
     * @param min minimum of L, C amd maximal matrices for {i,j} indices.
     * @param lInverse Linverse value of {i,j}
     * @param l L value of {i,j}
     * @param cInverse Cinverse value of {i,j}
     * @param c C value of {i,j}
     * @return int max
     */
    public static int forwardMaximal(int min, int lInverse, int l, int cInverse, int c) {
        int max = 0;
        for (int v = min; v >= 0; v--) {
            if (((lInverse - v) <= l) && ((cInverse - v) <= c)) {
                max = max + v;
                break;
            }
        }
        return max;
    }

    /**
     * Sub function of forward method. As described in Grund's thesis, the list of if-else
     * conditions of the recursive method.
     *
     * @param lInverse lInverse value of indices {i,j}
     * @param cInverse cInverse value of indices {i,j}
     * @param maximalX It is the maximal value, can be signed to indices {i,j}
     * @param i First Index
     * @param j Second Index
     * @param A int[][] adjancency matrix
     * @param indices ArrayList<Integer>
     * @throws CloneNotSupportedException
     * @throws CDKException
     * @throws IOException
     */
    public static int[][] forwardSub(
            int lInverse,
            int cInverse,
            int maximalX,
            int i,
            int j,
            int[][] A,
            int[] indices,
            boolean callForward)
            throws CloneNotSupportedException, CDKException, IOException {
        if (((lInverse - maximalX) <= L[i][j]) && ((cInverse - maximalX) <= C[i][j])) {
            A[i][j] = maximalX;
            A[j][i] = maximalX;
            if (i == (max.length - 2) && j == (max.length - 1)) {
                output.add(A);
                int[][] mat2 = new int[A.length][A.length];
                for (int k = 0; k < A.length; k++) {
                    for (int l = 0; l < A.length; l++) {
                        mat2[k][l] = A[k][l];
                    }
                }

                // A=addHydrogens(A,hIndex);
                if (connectivityTest(hIndex, addHydrogens(mat2, hIndex))) {
                    /** Just to avoid the duplicates for now, I use InChIs */
                    count++;
                    /**
                     * IAtomContainer mol= buildC(addHydrogens(mat2,hIndex)); String inchi=
                     * inchiGeneration(mol); if(!inchis.contains(inchi)) { outFile.write(mol);
                     * depict(mol,filedir+count+".png"); count++; inchis.add(inchi); }*
                     */
                }
                callForward = false;
                return nextStep(A, indices, callForward);
                // backwardDemo(A, indices);
            } else {
                callForward = true;
                indices = successor(indices, max.length);
                return nextStep(A, indices, callForward);
                // forwardDemo(A,indices);
            }
        } else {
            callForward = false;
            return nextStep(A, indices, callForward);
            // backwardDemo(A,indices);
        }
    }

    public static int gen = 0;
    public static int dup = 0;

    public static int[][] forwardSubRow(
            int lInverse,
            int cInverse,
            int maximalX,
            int i,
            int j,
            int[][] A,
            int[] indices,
            boolean callForward)
            throws CloneNotSupportedException, CDKException, IOException {
        if (((lInverse - maximalX) <= L[i][j]) && ((cInverse - maximalX) <= C[i][j])) {
            A[i][j] = maximalX;
            A[j][i] = maximalX;
            if (i == (max.length - 2) && j == (max.length - 1)) {
                if (canonicalTest(A)) {
                    output.add(A);
                    // A=addHydrogens(A,hIndex);
                    int[][] mat2 = new int[A.length][A.length];
                    for (int k = 0; k < A.length; k++) {
                        for (int l = 0; l < A.length; l++) {
                            mat2[k][l] = A[k][l];
                        }
                    }
                    // IAtomContainer molden= buildC(addHydrogens(mat2,hIndex));
                    if (connectivityTest(hIndex, addHydrogens(mat2, hIndex))) {
                        pWriter.println("canonical matrix" + " " + count);
                        for (int s = 0; s < 6; s++) {
                            for (int k = 0; k < s; k++) {
                                pWriter.print(" ");
                            }
                            for (int k = s + 1; k < 6; k++) {
                                pWriter.print(mat2[s][k]);
                            }
                            pWriter.println();
                        }
                        /**
                         * IAtomContainer mol= buildC(addHydrogens(mat2,hIndex)); String inchi=
                         * inchiGeneration(mol); if(!inchis.contains(inchi)) { outFile.write(mol);
                         * depict(mol,filedir+count+".png"); count++; inchis.add(inchi); }else {
                         * depict(mol,filedir+dup+"duplicate.png"); dup++; }*
                         */
                        IAtomContainer mol = buildC(addHydrogens(mat2, hIndex));
                        outFile.write(mol);
                        // depict(mol,filedir+count+".png");
                        count++;
                    }
                }
                callForward = false;
                return nextStep(A, indices, callForward);
            } else {
                /**
                 * callForward=true; indices=successor(indices,max.length); updateR(indices); return
                 * nextStep(A, indices, callForward);*
                 */
                if (indices[0] == findZ(r) && indices[1] == (max.length - 1)) {
                    callForward = canonicalTest(A);
                    if (callForward) {
                        indices = successor(indices, max.length);
                        updateR(indices);
                    }
                    return nextStep(A, indices, callForward);
                } else {
                    if (i < findZ(r) && j == (max.length - 1)) {
                        return A;
                    } else {
                        indices = successor(indices, max.length);
                        updateR(indices);
                        return nextStep(A, indices, true);
                    }
                }
            }
        } else {
            callForward = false;
            return nextStep(A, indices, callForward);
        }
    }

    /**
     * The third step in Grund 3.2.3.
     *
     * <p>Backward step in the algorithm.
     *
     * @param A int[][] adjacency matrix
     * @param indices ArrayList<Integer> indices
     * @throws IOException
     * @throws CloneNotSupportedException
     * @throws CDKException
     */
    public static boolean flag = true;

    public static int[][] backwardDemo(int[][] A, int[] indices, boolean callForward)
            throws IOException, CloneNotSupportedException, CDKException {
        int i = indices[0];
        int j = indices[1];

        if (i == 0 && j == 1) {
            flag = false;
            return A;
        } else {
            indices = predecessor(indices, max.length);
            updateR(indices);
            i = indices[0];
            j = indices[1];
            int x = A[i][j];
            int l2 = LInverse(degrees, i, j, A);
            int c2 = CInverse(degrees, i, j, A);
            if (x > 0 && (backwardCriteria(x, l2, L[i][j]) && backwardCriteria(x, c2, C[i][j]))) {
                // if(x>0 && (((l2-(x-1))<=L[i][j]) && ((c2-(x-1))<=C[i][j]))) {
                A[i][j] = (x - 1);
                A[j][i] = (x - 1);
                indices = successor(indices, max.length);
                updateR(indices);
                callForward = true;
                return nextStep(A, indices, callForward);
                // return nextStepStrip(y,z,A,indices,callForward,entryChanges);
                // forwardDemo(A, indices);
            } else {
                callForward = false;
                return nextStep(A, indices, callForward);
                // return nextStepStrip(y,z,A,indices,callForward,entryChanges);
                // backwardDemo(A,indices);
            }
        }
    }

    public static void updateR(int[] indices) {
        int y = findY(r);
        int z = findZ(r);
        if (indices[0] < y) {
            r--;
        } else if (indices[0] > z) {
            r++;
        }
    }

    public static int[][] nextStep(int[][] A, int[] indices, boolean callForward)
            throws IOException, CloneNotSupportedException, CDKException {
        if (callForward) {
            return forwardRow(A, indices, callForward);
        } else {
            return backwardDemo(A, indices, callForward);
        }
    }

    public static int[][] nextStepStrip(int[][] A, int[] indices, boolean callForward)
            throws IOException, CloneNotSupportedException, CDKException {
        if (callForward) {
            return forwardRow(A, indices, callForward);
        } else {
            return backwardDemo(A, indices, callForward);
        }
    }

    /**
     * The third line of the backward method in Grund 3.2.3. The criteria to decide which function
     * is needed: forward or backward.
     *
     * @param x the value in the adjacency matrix A[i][j]
     * @param lInverse lInverse value of indices {i,j}
     * @param l
     * @return
     */
    public static boolean backwardCriteria(int x, int lInverse, int l) {
        boolean check = false;
        int newX = (x - 1);
        if ((lInverse - newX) <= l) {
            check = true;
        }
        return check;
    }

    public static int[] successor(int[] indices, int size) {
        int i0 = indices[0];
        int i1 = indices[1];
        if (i1 < (size - 1)) {
            indices[0] = i0;
            indices[1] = (i1 + 1);
        } else if (i0 < (size - 2) && i1 == (size - 1)) {
            indices[0] = (i0 + 1);
            indices[1] = (i0 + 2);
        }
        return indices;
    }

    public static int[] predecessor(int[] indices, int size) {
        int i0 = indices[0];
        int i1 = indices[1];
        if (i0 == i1 - 1) {
            indices[0] = (i0 - 1);
            indices[1] = (size - 1);
        } else {
            indices[0] = i0;
            indices[1] = (i1 - 1);
        }
        return indices;
    }

    public static ArrayList<ArrayList<Integer>> partlar = new ArrayList<ArrayList<Integer>>();

    public static void partlar(ArrayList<Integer> partition, int total) {
        for (int i = 0; i < (total - 1); i++) {
            partition = canonicalPartition(i, partition);
            partlar.add(partition);
        }
    }

    public static boolean biggest = true;

    public static int[] getCyclePermutation(int[] max, int[] check, ArrayList<Integer> partition) {
        int[] values = idValues(sum(partition));
        int i = 0;
        int size = sum(partition);
        Permutation perm = new Permutation(size);
        if (!equalSetCheck(max, check, partition)) {
            return values;
            // return perm;
        } else {
            for (Integer p : partition) {
                Integer[] can = getBlocks(max, i, p + i);
                Integer[] non = getBlocks(check, i, p + i);
                // perm = getPerfmutation(can, non, i, perm);
                values = getCyclesList(can, non, i, values);
                // values= permutation(can, non, i, values);
                i = i + p;
            }
            return values;
            // return perm;
        }
    }

    public static Permutation getCanonicalPermutation(
            int[] max, int[] check, ArrayList<Integer> partition) {
        int size = 6;
        int[] cycles = getCyclePermutation(max, check, partition);
        int[] perm = new int[size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == cycles[j]) {
                    perm[i] = j;
                }
            }
        }
        return new Permutation(perm);
    }

    public static Permutation getPermutation(
            Integer[] max, Integer[] non, int index, Permutation permutation) {
        int i = 0;
        int permutationIndex = 0;
        List<Integer> indices = new ArrayList<Integer>();
        while (i < max.length && max[i] != 0) {
            // denemepermutationIndex = findMatch(non, max[i],i);
            if (i != permutationIndex) {
                non = permuteArray(non, i, permutationIndex);
            }
            indices.add(permutationIndex);
            // perm=buildCycle((i+index),(permutationIndex+index),perm);
            i++;
        }
        return getCyclePermutation(indices, index, permutation);
    }

    public static int[] getCyclesList(Integer[] max, Integer[] non, int index, int[] values) {
        int i = 0;
        int permutationIndex = 0;
        while (i < max.length && max[i] != 0) {
            if (max[i] != non[i]) {
                permutationIndex = findMatch(max, non, max[i], i);
                if (i != permutationIndex) {
                    non = permuteArray(non, i, permutationIndex);
                }
                int temp = values[i + index];
                values[i + index] = values[permutationIndex + index];
                values[permutationIndex + index] = temp;
                // values[i+index]=permutationIndex+index;
                /**
                 * sonuncuyu sanrm ele almamalym. 45, 56 ornegi gibi dsun 64 u act ettirmiyorum ki.
                 *
                 * <p>okurken de bu cycle dan basla en sondan cycle olarak dusun. table da cyclelarm
                 * bunlar olacak perm da deil.
                 */
            }
            i++;
        }
        return values;
    }

    public static int[] visit = new int[size];

    public static int[] permutation(Integer[] max, Integer[] non, int index, int[] values) {
        int i = 0;
        int next = 1;
        int entry = 0;
        int cycleStarts = 0;
        visit = new int[max.length];
        while (i < non.length) {
            if (visit[i] != 1) {
                entry = max[i];
                cycleStarts = i;
                List<Integer> cycle = new ArrayList<Integer>();
                cycle.add(cycleStarts);
                while (next != cycleStarts) {
                    next = findMatch(max, non, entry, i);
                    entry = max[next];
                    if (cycle.contains(next)) {
                        break;
                    } else {
                        cycle.add(next);
                    }
                }
                if (cycle.size() != 1) {
                    setCycles(values, cycle, index);
                }
            }
            i++;
        }
        return values;
    }

    public static int[] setCycles(int[] values, List<Integer> cycle, int index) {
        int size = cycle.size();
        for (int i = 0; i < size - 1; i++) {
            values[(cycle.get(i) + index)] = (cycle.get(i + 1) + index);
        }
        values[(cycle.get(size - 1) + index)] = (cycle.get(0) + index);
        return values;
    }

    /**
     * public static int findMatch(Integer[] max, Integer[] non, int value, int index) { int
     * size=max.length; for(int i=0;i<size;i++) { if(visit[i]!=1 && max[i]==value) { if(index!=i) {
     * if(max[i]!=non[i]) { index=i; visit[i]=1; break; } }else { index=i; visit[i]=1; break; } } }
     * return index; }*
     */
    public static Permutation getCyclePermutation(
            List<Integer> list, int index, Permutation permutation) {
        if (list.size() != 0) {
            for (int i = list.size() - 1; i >= 0; i--) {
                permutation = buildCycle(i + index, list.get(i) + index, permutation);
            }
        }
        return permutation;
    }

    public static Permutation buildCycle(int i, int j, Permutation perm) {
        int[] values = perm.getValues();
        int temp = i;
        values[i] = values[j];
        values[j] = temp;
        return new Permutation(values);
    }

    /**
     * public static int findMatch(Integer[] array, int value, int start) { int size=array.length;
     * int index=start; for(int i=start;i<size;i++) { if(array[i]==value) { index=i; break; } }
     * return index; }*
     */
    public static int findMatch(Integer[] max, Integer[] non, int value, int start) {
        int size = non.length;
        int index = start;
        for (int i = start; i < size; i++) {
            if (non[i] == value) {
                if (max[i] != non[i]) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    public static Integer[] permuteArray(Integer[] array, int i, int j) {
        int temp = 0;
        temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        return array;
    }

    public static List<int[]> copyList(List<int[]> list) {
        List<int[]> newList = new ArrayList<int[]>();
        for (int[] i : list) {
            newList.add(i);
        }
        return newList;
    }

    public static List<int[]> addElement(List<int[]> list, int[] add) {
        List<int[]> newList = copyList(list);
        newList.add(add);
        return newList;
    }

    public static List<int[]> buildList(int[] entry) {
        List<int[]> list = new ArrayList<int[]>();
        list.add(entry);
        return list;
    }

    /**
     * deneme
     *
     * <p>public static void lCombine(List<List<int[]>> outputs,List<int[]> list, int total) throws
     * CloneNotSupportedException { if(list.size()==(total)) { outputs.add(list); }else { for(int[]
     * i: repL.get(list.size())) { lCombine(outputs,addElement(list, i),total); } } }*
     */
    public static void main(String[] args)
            throws CloneNotSupportedException, CDKException, IOException {
        FileWriter fWriter =
                new FileWriter("C:\\Users\\mehme\\Desktop\\No-Backup Zone\\outputs\\output.txt");
        pWriter = new PrintWriter(fWriter);
        /**
         * int[][] mat= new int[6][6]; mat[0][1]=3; mat[0][2]=1; mat[1][0]=3; mat[1][3]=1;
         * mat[2][0]=1; mat[2][4]=1; mat[2][5]=1; mat[3][1]=1; mat[3][4]=2; mat[4][2]=1;
         * mat[4][3]=2; mat[5][2]=1; ArrayList<Integer> part= new ArrayList<Integer>(); part.add(2);
         * part.add(3); part.add(1); initialPartition=part; partitionList.add(initialPartition);
         * Permutation perm= new Permutation(1,0,3,2,4,5); formerPermutations.add(idPermutation(6));
         * formerPermutations.add(perm); ArrayList<Integer> part2= new ArrayList<Integer>();
         * part2.add(1); part2.add(1); part2.add(1); part2.add(2); part2.add(1); ArrayList<Integer>
         * part3= new ArrayList<Integer>(); part3.add(1); part3.add(1); part3.add(1); part3.add(1);
         * part3.add(1); part3.add(1); partitionList.add(part2); partitionList.add(part3);*
         */
        // System.out.println(blockTest(1,mat));
        run("C6H6", "C:\\Users\\mehme\\Desktop\\No-Backup Zone\\outputs\\");
        pWriter.close();
    }
}
