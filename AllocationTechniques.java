public abstract class AllocationTechniques {

    abstract boolean allocate(VirtualFileSystem disk, String path, int size);

    abstract boolean deallocate(VirtualFileSystem disk, String path);

}
