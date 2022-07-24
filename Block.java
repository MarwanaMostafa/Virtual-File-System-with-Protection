import java.util.ArrayList;

public class Block {
    int indexBlock;
    int AntoherIndexBlock=-1;
    static final int sizeBlock = 1;
    boolean isDeallocated = true;
    ArrayList<Integer> pointerToBlock = new ArrayList<Integer>();

    public Block(int index) {
        this.indexBlock = index;
    }

    public Block(int index, boolean status) {
        this.indexBlock = index;
        this.isDeallocated = status;
        pointerToBlock = new ArrayList<>();
    }

    public void setAntoherIndexBlock(int AnotherIndex) {
        AntoherIndexBlock = AnotherIndex;
    }

    public int getAntoherIndexBlock() {
        return AntoherIndexBlock;
    }

    public boolean isDeallocated() {
        return isDeallocated;
    }

    public void setDeallocated(boolean isDeallocated) {
        this.isDeallocated = isDeallocated;
    }

    public void setIndexBlock(int indexBlock) {
        this.indexBlock = indexBlock;
    }

    public void setPointerToBlock(ArrayList<Integer> pointerToBlock) {
        this.pointerToBlock = pointerToBlock;
    }

    public int getIndexBlock() {
        return indexBlock;
    }

    public ArrayList<Integer> getPointerToBlock() {
        return pointerToBlock;
    }

    public static int getSizeblock() {
        return sizeBlock;
    }
}
