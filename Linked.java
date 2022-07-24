
public class Linked extends AllocationTechniques {

    @Override
    boolean allocate(VirtualFileSystem disk, String path, int size) {

        if (disk.getSizeSystem() <= size) {
            // System.out.println("NO AVLIABLE SPACCE");
            return false;
        }

        int NumOFBlcoks = size;
        int IndexBLOCK = 0;
        boolean FirstBlock = false;
        int startBlock = 0;
        for (int i = 0; i < disk.getFreeBlocksIndex().size(); i++) {
            if (disk.getBlocks().get(i).isDeallocated()) {
                if (!FirstBlock) {
                    disk.getBlocks().get(i).setDeallocated(false);
                    disk.getBlocks().get(i).setIndexBlock(i);
                    startBlock = i;
                    disk.getFreeBlocksIndex().set(i, -1);
                    disk.getBlocks().get(i).setAntoherIndexBlock(-1);// if size of file is one block then
                    // first block is last block in linked so will point to -1
                    if (NumOFBlcoks == 1)
                        IndexBLOCK = -1;
                    else
                        IndexBLOCK = i;
                    FirstBlock = true;
                } else if (NumOFBlcoks == 1) {// assign negative 1 for end block
                    disk.getBlocks().get(i).setDeallocated(false);
                    disk.getBlocks().get(i).setIndexBlock(i);
                    disk.getBlocks().get(IndexBLOCK).setAntoherIndexBlock(i);// before last block
                    disk.getBlocks().get(i).setAntoherIndexBlock(-1);// before last block
                    IndexBLOCK = i;
                    disk.getFreeBlocksIndex().set(i, -1);
                } else {
                    disk.getBlocks().get(i).setDeallocated(false);
                    disk.getBlocks().get(i).setIndexBlock(i);
                    disk.getBlocks().get(IndexBLOCK).setAntoherIndexBlock(i);
                    IndexBLOCK = i;
                    disk.getFreeBlocksIndex().set(i, -1);
                }
                NumOFBlcoks--;
                if (NumOFBlcoks == 0)
                    break;
            }
        }
        // remove from free block NBlock for new file
        disk.setFreeBlocks(disk.getFreeBlocks() - size);

        disk.setAllocatedBlocks(disk.getAllocatedBlocks() + size);

        // for (int i = startBlock; i < size + startBlock; i++) {
        // System.out.println("Start BLOCK IS " +
        // disk.getBlocks().get(i).getIndexBlock());
        // System.out.println("Point BLock IS " +
        // disk.getBlocks().get(i).getAntoherIndexBlock());
        // }
        IndexTable table = new IndexTable(path, startBlock);
        table.setAnotherIndex(IndexBLOCK);
        // System.out.println("INDEX TABLE BLOCK IS " +
        // table.getIndex());
        // System.out.println("ENDBLOCK IS TABLE IS " +
        // table.getAnotherIndex());

        disk.getAllocatedBlocksTable().add(table);
        return true;
    }

    @Override
    boolean deallocate(VirtualFileSystem disk, String path) {
        int NumberOfStartBlock = 0;
        for (int i = 0; i < disk.getAllocatedBlocksTable().size(); i++)
            if (disk.getAllocatedBlocksTable().get(i).getPath().equals(path))
                NumberOfStartBlock = disk.getAllocatedBlocksTable().get(i).getIndex();

        disk.getFreeBlocksIndex().set(disk.getBlocks().get(NumberOfStartBlock).getIndexBlock(), NumberOfStartBlock);
        disk.getBlocks().get(NumberOfStartBlock).setDeallocated(true);
        disk.getBlocks().get(NumberOfStartBlock).setIndexBlock(-1);
        disk.getAllocatedBlocksTable().remove(NumberOfStartBlock);

        int size = 1;
        while (NumberOfStartBlock != -1) {// if we have linke like this 0=>5=>10=>-1 above code is 0
            int nextBlock = disk.getBlocks().get(NumberOfStartBlock).getAntoherIndexBlock();// =>5
            disk.getBlocks().get(NumberOfStartBlock).setAntoherIndexBlock(-1);

            if (nextBlock != -1) {
                disk.getFreeBlocksIndex().set(nextBlock, nextBlock);
                disk.getBlocks().get(nextBlock).setDeallocated(true);
                disk.getBlocks().get(nextBlock).setIndexBlock(-1);
                size++;
            }
            NumberOfStartBlock = nextBlock;// =>5 and next iterative nextblock will be =>10 ...
        }
        disk.setFreeBlocks(disk.getFreeBlocks() + size);

        disk.setAllocatedBlocks(disk.getAllocatedBlocks() - size);

        return true;
    }

}
