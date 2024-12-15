package alabaster.hearthandharvest.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

@SuppressWarnings("deprecation")
public class TreeTapperBlock extends Block {

        public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
        public static final IntegerProperty SAP_LEVEL = IntegerProperty.create("sap_level", 0, 4);

        public TreeTapperBlock(Properties properties) {
                super(properties);
        }
}