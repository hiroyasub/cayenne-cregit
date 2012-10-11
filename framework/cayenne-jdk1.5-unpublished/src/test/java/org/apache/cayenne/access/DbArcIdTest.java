begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|ObjectId
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
name|map
operator|.
name|DbEntity
import|;
end_import

begin_class
specifier|public
class|class
name|DbArcIdTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testHashCode
parameter_list|()
block|{
name|DbEntity
name|e
init|=
operator|new
name|DbEntity
argument_list|(
literal|"X"
argument_list|)
decl_stmt|;
name|DbArcId
name|id1
init|=
operator|new
name|DbArcId
argument_list|(
name|e
argument_list|,
operator|new
name|ObjectId
argument_list|(
literal|"x"
argument_list|,
literal|"k"
argument_list|,
literal|"v"
argument_list|)
argument_list|,
literal|"r1"
argument_list|)
decl_stmt|;
name|int
name|h1
init|=
name|id1
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|h1
argument_list|,
name|id1
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|h1
argument_list|,
name|id1
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|DbArcId
name|id1_eq
init|=
operator|new
name|DbArcId
argument_list|(
name|e
argument_list|,
operator|new
name|ObjectId
argument_list|(
literal|"x"
argument_list|,
literal|"k"
argument_list|,
literal|"v"
argument_list|)
argument_list|,
literal|"r1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|h1
argument_list|,
name|id1_eq
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|DbArcId
name|id2
init|=
operator|new
name|DbArcId
argument_list|(
name|e
argument_list|,
operator|new
name|ObjectId
argument_list|(
literal|"x"
argument_list|,
literal|"k"
argument_list|,
literal|"v"
argument_list|)
argument_list|,
literal|"r2"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|h1
operator|==
name|id2
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|DbArcId
name|id3
init|=
operator|new
name|DbArcId
argument_list|(
name|e
argument_list|,
operator|new
name|ObjectId
argument_list|(
literal|"y"
argument_list|,
literal|"k"
argument_list|,
literal|"v"
argument_list|)
argument_list|,
literal|"r1"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|h1
operator|==
name|id3
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
name|DbEntity
name|e
init|=
operator|new
name|DbEntity
argument_list|(
literal|"X"
argument_list|)
decl_stmt|;
name|DbArcId
name|id1
init|=
operator|new
name|DbArcId
argument_list|(
name|e
argument_list|,
operator|new
name|ObjectId
argument_list|(
literal|"x"
argument_list|,
literal|"k"
argument_list|,
literal|"v"
argument_list|)
argument_list|,
literal|"r1"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|id1
operator|.
name|equals
argument_list|(
name|id1
argument_list|)
argument_list|)
expr_stmt|;
name|DbArcId
name|id1_eq
init|=
operator|new
name|DbArcId
argument_list|(
name|e
argument_list|,
operator|new
name|ObjectId
argument_list|(
literal|"x"
argument_list|,
literal|"k"
argument_list|,
literal|"v"
argument_list|)
argument_list|,
literal|"r1"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|id1
operator|.
name|equals
argument_list|(
name|id1_eq
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|id1_eq
operator|.
name|equals
argument_list|(
name|id1
argument_list|)
argument_list|)
expr_stmt|;
name|DbArcId
name|id2
init|=
operator|new
name|DbArcId
argument_list|(
name|e
argument_list|,
operator|new
name|ObjectId
argument_list|(
literal|"x"
argument_list|,
literal|"k"
argument_list|,
literal|"v"
argument_list|)
argument_list|,
literal|"r2"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|id1
operator|.
name|equals
argument_list|(
name|id2
argument_list|)
argument_list|)
expr_stmt|;
name|DbArcId
name|id3
init|=
operator|new
name|DbArcId
argument_list|(
name|e
argument_list|,
operator|new
name|ObjectId
argument_list|(
literal|"y"
argument_list|,
literal|"k"
argument_list|,
literal|"v"
argument_list|)
argument_list|,
literal|"r1"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|id1
operator|.
name|equals
argument_list|(
name|id3
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|id1
operator|.
name|equals
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

