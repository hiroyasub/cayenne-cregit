begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|testdo
operator|.
name|testmap
operator|.
name|auto
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneDataObject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|Property
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|testdo
operator|.
name|testmap
operator|.
name|Bag
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|testdo
operator|.
name|testmap
operator|.
name|Ball
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|testdo
operator|.
name|testmap
operator|.
name|BoxInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|testdo
operator|.
name|testmap
operator|.
name|Thing
import|;
end_import

begin_comment
comment|/**  * Class _Box was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Box
extends|extends
name|CayenneDataObject
block|{
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|NAME_PROPERTY
init|=
literal|"name"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|BAG_PROPERTY
init|=
literal|"bag"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|BALLS_PROPERTY
init|=
literal|"balls"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|BOX_INFO_PROPERTY
init|=
literal|"boxInfo"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|THINGS_PROPERTY
init|=
literal|"things"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ID_PK_COLUMN
init|=
literal|"ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|NAME
init|=
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Bag
argument_list|>
name|BAG
init|=
operator|new
name|Property
argument_list|<
name|Bag
argument_list|>
argument_list|(
literal|"bag"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|List
argument_list|<
name|Ball
argument_list|>
argument_list|>
name|BALLS
init|=
operator|new
name|Property
argument_list|<
name|List
argument_list|<
name|Ball
argument_list|>
argument_list|>
argument_list|(
literal|"balls"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|BoxInfo
argument_list|>
name|BOX_INFO
init|=
operator|new
name|Property
argument_list|<
name|BoxInfo
argument_list|>
argument_list|(
literal|"boxInfo"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|List
argument_list|<
name|Thing
argument_list|>
argument_list|>
name|THINGS
init|=
operator|new
name|Property
argument_list|<
name|List
argument_list|<
name|Thing
argument_list|>
argument_list|>
argument_list|(
literal|"things"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"name"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setBag
parameter_list|(
name|Bag
name|bag
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"bag"
argument_list|,
name|bag
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Bag
name|getBag
parameter_list|()
block|{
return|return
operator|(
name|Bag
operator|)
name|readProperty
argument_list|(
literal|"bag"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToBalls
parameter_list|(
name|Ball
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"balls"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromBalls
parameter_list|(
name|Ball
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"balls"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|List
argument_list|<
name|Ball
argument_list|>
name|getBalls
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|Ball
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"balls"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setBoxInfo
parameter_list|(
name|BoxInfo
name|boxInfo
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"boxInfo"
argument_list|,
name|boxInfo
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BoxInfo
name|getBoxInfo
parameter_list|()
block|{
return|return
operator|(
name|BoxInfo
operator|)
name|readProperty
argument_list|(
literal|"boxInfo"
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|List
argument_list|<
name|Thing
argument_list|>
name|getThings
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|Thing
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"things"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

