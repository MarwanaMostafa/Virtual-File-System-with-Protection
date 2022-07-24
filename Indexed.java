import java.util.ArrayList;

public class Indexed extends AllocationTechniques {

    @Override
    boolean allocate(VirtualFileSystem disk, String path, int size) {

        if (disk.getSizeSystem() <= size) {
            // System.out.println("NO AVLIABLE SPACCE");
            return false;
        }
        int NumOFBlcoks = size + 1;
        int NumOFBlcoks2 = size + 1;
        int IndexBLOCK = 0;
        boolean IsStart = false;
        for (int i = 0; i < disk.getFreeBlocksIndex().size(); i++) {
            if (disk.getBlocks().get(i).isDeallocated()) {
                if (!IsStart) {
                    IndexBLOCK = i;
                    IsStart = true;
                } else
                    disk.getBlocks().get(IndexBLOCK).getPointerToBlock().add(i);

                disk.getBlocks().get(i).setDeallocated(false);

                disk.getBlocks().get(i).setIndexBlock(i);

                disk.getFreeBlocksIndex().set(i, -1);
                NumOFBlcoks2--;
                if (NumOFBlcoks2 == 0)
                    break;
            }
        }
        // remove from free block NBlock for new file
        disk.setFreeBlocks(disk.getFreeBlocks() - NumOFBlcoks);

        disk.setAllocatedBlocks(disk.getAllocatedBlocks() + NumOFBlcoks);
        IndexTable table = new IndexTable(path, IndexBLOCK);
        disk.getAllocatedBlocksTable().add(table);

        return true;
    }

    @Override
    boolean deallocate(VirtualFileSystem disk, String path) {
        int NumberOfIndexBLOCK = 0;

        for (int i = 0; i < disk.getAllocatedBlocksTable().size(); i++) {
            if (disk.getAllocatedBlocksTable().get(i).getPath().equals(path)) {
                NumberOfIndexBLOCK = disk.getAllocatedBlocksTable().get(i).getIndex();

            }
        }
        // System.out.println(virtualFileSystem.getBlocks().get(1).getPointerToBlock().get(0));


        for (int i = 0; i < disk.getBlocks().get(NumberOfIndexBLOCK).getPointerToBlock().size(); i++) {
            // because when we allocate we change indexfree block to-1
            disk.getFreeBlocksIndex().set(disk.getBlocks().get(NumberOfIndexBLOCK).getPointerToBlock().get(i),
                    disk.getBlocks().get(NumberOfIndexBLOCK).getPointerToBlock().get(i));
            disk.getBlocks().get(disk.getBlocks().get(NumberOfIndexBLOCK).getPointerToBlock().get(i))
                    .setDeallocated(true);

            disk.getBlocks().get(disk.getBlocks().get(NumberOfIndexBLOCK).getPointerToBlock().get(i))
                    .setIndexBlock(-1);
        }

        disk.setFreeBlocks(
                disk.getFreeBlocks() + (disk.getBlocks().get(NumberOfIndexBLOCK).getPointerToBlock().size() + 1));
        disk.setAllocatedBlocks(
                disk.getAllocatedBlocks() - (disk.getBlocks().get(NumberOfIndexBLOCK).getPointerToBlock().size() + 1));

        disk.getFreeBlocksIndex().set(NumberOfIndexBLOCK, NumberOfIndexBLOCK);

        disk.getAllocatedBlocksTable().remove(NumberOfIndexBLOCK);

        disk.getBlocks().get(NumberOfIndexBLOCK).setDeallocated(true);
        disk.getBlocks().get(NumberOfIndexBLOCK).setIndexBlock(-1);
        disk.getBlocks().get(NumberOfIndexBLOCK).setPointerToBlock(new ArrayList<>());
        return true;
    }

}
