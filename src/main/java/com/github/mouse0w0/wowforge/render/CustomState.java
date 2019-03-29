package com.github.mouse0w0.wowforge.render;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class CustomState implements IBlockState {
    IBlockState original;

    public CustomState(IBlockState original) {
        this.original = original;
    }

    @Override
    public boolean onBlockEventReceived(World worldIn, BlockPos pos, int id, int param) {
        return original.onBlockEventReceived(worldIn, pos, id, param);
    }

    @Override
    public void neighborChanged(World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        original.neighborChanged(worldIn, pos, blockIn, fromPos);
    }

    @Override
    public Material getMaterial() {
        return original.getMaterial();
    }

    @Override
    public boolean isFullBlock() {
        return original.isFullBlock();
    }

    @Override
    public boolean canEntitySpawn(Entity entityIn) {
        return original.canEntitySpawn(entityIn);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getLightOpacity() {
        return original.getLightOpacity();
    }

    @Override
    public int getLightOpacity(IBlockAccess world, BlockPos pos) {
        return original.getLightOpacity(world, pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getLightValue() {
        return original.getLightValue();
    }

    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos) {
        return original.getLightValue(world, pos);
    }

    @Override
    public boolean isTranslucent() {
        return original.isTranslucent();
    }

    @Override
    public boolean useNeighborBrightness() {
        return original.useNeighborBrightness();
    }

    @Override
    public MapColor getMapColor(IBlockAccess p_185909_1_, BlockPos p_185909_2_) {
        return original.getMapColor(p_185909_1_, p_185909_2_);
    }

    @Override
    public IBlockState withRotation(Rotation rot) {
        return original.withRotation(rot);
    }

    @Override
    public IBlockState withMirror(Mirror mirrorIn) {
        return original.withMirror(mirrorIn);
    }

    @Override
    public boolean isFullCube() {
        return original.isFullCube();
    }

    @Override
    public boolean hasCustomBreakingProgress() {
        return original.hasCustomBreakingProgress();
    }

    @Override
    public EnumBlockRenderType getRenderType() {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
        // Only changed method
    }

    @Override
    public int getPackedLightmapCoords(IBlockAccess source, BlockPos pos) {
        return original.getPackedLightmapCoords(source, pos);
    }

    @Override
    public float getAmbientOcclusionLightValue() {
        return original.getAmbientOcclusionLightValue();
    }

    @Override
    public boolean isBlockNormalCube() {
        return isBlockNormalCube();
    }

    @Override
    public boolean isNormalCube() {
        return original.isNormalCube();
    }

    @Override
    public boolean canProvidePower() {
        return original.canProvidePower();
    }

    @Override
    public int getWeakPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return original.getWeakPower(blockAccess, pos, side);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return original.hasComparatorInputOverride();
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        return original.getComparatorInputOverride(worldIn, pos);
    }

    @Override
    public float getBlockHardness(World worldIn, BlockPos pos) {
        return original.getBlockHardness(worldIn, pos);
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World worldIn, BlockPos pos) {
        return original.getPlayerRelativeBlockHardness(player, worldIn, pos);
    }

    @Override
    public int getStrongPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return original.getStrongPower(blockAccess, pos, side);
    }

    @Override
    public IBlockState getActualState(IBlockAccess blockAccess, BlockPos pos) {
        return original.getActualState(blockAccess, pos);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        return original.getSelectedBoundingBox(worldIn, pos);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, BlockPos pos, EnumFacing facing) {
        return original.shouldSideBeRendered(blockAccess, pos, facing);
    }

    @Override
    public boolean isOpaqueCube() {
        return original.isOpaqueCube();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockAccess worldIn, BlockPos pos) {
        AxisAlignedBB box = original.getCollisionBoundingBox(worldIn, pos);
        if (box != null)
            return box.grow(10);
        return null;
    }

    @Override
    public void addCollisionBoxToList(World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185908_6_) {
        original.addCollisionBoxToList(worldIn, pos, entityBox, collidingBoxes, entityIn, p_185908_6_);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockAccess blockAccess, BlockPos pos) {
        return original.getBoundingBox(blockAccess, pos).grow(10);
    }

    @Override
    public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        return original.collisionRayTrace(worldIn, pos, start, end);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isTopSolid() {
        return original.isTopSolid();
    }

    @Override
    public boolean doesSideBlockRendering(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return original.doesSideBlockRendering(world, pos, side);
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return original.isSideSolid(world, pos, side);
    }

    @Override
    public boolean doesSideBlockChestOpening(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return original.doesSideBlockChestOpening(world, pos, side);
    }

    @Override
    public Vec3d getOffset(IBlockAccess access, BlockPos pos) {
        return original.getOffset(access, pos);
    }

    @Override
    public boolean causesSuffocation() {
        return original.causesSuffocation();
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
        return original.getBlockFaceShape(worldIn, pos, facing);
    }

    @Override
    public Collection<IProperty<?>> getPropertyKeys() {
        return original.getPropertyKeys();
    }

    @Override
    public <T extends Comparable<T>> T getValue(IProperty<T> property) {
        return original.getValue(property);
    }

    @Override
    public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value) {
        return original.withProperty(property, value);
    }

    @Override
    public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> property) {
        return original.cycleProperty(property);
    }

    @Override
    public ImmutableMap<IProperty<?>, Comparable<?>> getProperties() {
        return original.getProperties();
    }

    @Override
    public Block getBlock() {
        return original.getBlock();
    }

    @Override
    public EnumPushReaction getPushReaction() {
        return original.getPushReaction();
    }

}
