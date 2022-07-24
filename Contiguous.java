public class Contiguous extends AllocationTechniques {

    @Override
    boolean allocate(VirtualFileSystem disk, String path, int size) {

        if (disk.getSizeSystem() <= size || disk.getFreeBlocks() <= size) {
            // System.out.println("NO AVLIABLE SPACCE");
            return false;
        }
        int startFreeBlock = -1;
        int countFreeContiguousBlock = 0;

        for (int i = 0; i < disk.getFreeBlocksIndex().size(); i++) {
            if (disk.getBlocks().get(i).isDeallocated()) {
                if (startFreeBlock == -1)
                    startFreeBlock = i;
                countFreeContiguousBlock++;
            } else {
                startFreeBlock = -1;
                countFreeContiguousBlock = 0;
            }
            if (countFreeContiguousBlock == size)
                break;
        }
        if (countFreeContiguousBlock < size) {
            System.out.println("no Avaliable space");
            return false;
        }
        for (int i = 0; i < size; i++) {

            disk.getBlocks().get(startFreeBlock + i).setDeallocated(false);
            disk.getBlocks().get(startFreeBlock + i).setIndexBlock(i + startFreeBlock);
            disk.getFreeBlocksIndex().set(startFreeBlock + i, -1);
        }
        disk.setFreeBlocks(disk.getFreeBlocks() - size);

        disk.setAllocatedBlocks(disk.getAllocatedBlocks() + size);
        IndexTable table = new IndexTable(path, startFreeBlock);
        table.setAnotherIndex(size);// length
        disk.getAllocatedBlocksTable().add(table);
        return true;
    }

    @Override
    boolean deallocate(VirtualFileSystem disk, String path) {
        int NumberOfStartBlock = 0;
        int length = 0;
        for (int i = 0; i < disk.getAllocatedBlocksTable().size(); i++) {
            if (disk.getAllocatedBlocksTable().get(i).getPath().equals(path)) {
                NumberOfStartBlock = disk.getAllocatedBlocksTable().get(i).getIndex();
                length = disk.getAllocatedBlocksTable().get(i).getAnotherIndex();
            }
        }
        for (int i = 0; i < length; i++) {

            disk.getBlocks().get(NumberOfStartBlock + i).setDeallocated(true);
            disk.getBlocks().get(NumberOfStartBlock + i).setIndexBlock(-1);
            disk.getFreeBlocksIndex().set(NumberOfStartBlock + i, NumberOfStartBlock + i);
        }
        disk.setFreeBlocks(disk.getFreeBlocks() + length);
        disk.setAllocatedBlocks(disk.getAllocatedBlocks() - length);
        disk.getAllocatedBlocksTable().remove(NumberOfStartBlock);

        return true;
    }
}
