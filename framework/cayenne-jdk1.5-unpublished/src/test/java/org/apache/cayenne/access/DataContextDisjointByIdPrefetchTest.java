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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|PersistenceState
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
name|ValueHolder
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
name|di
operator|.
name|Inject
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
name|query
operator|.
name|PrefetchTreeNode
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
name|query
operator|.
name|SelectQuery
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|test
operator|.
name|jdbc
operator|.
name|TableHelper
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
name|Box
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|unit
operator|.
name|di
operator|.
name|DataChannelInterceptor
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
name|unit
operator|.
name|di
operator|.
name|UnitTestClosure
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|ServerCase
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|UseServerRuntime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

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
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|ExpressionFactory
operator|.
name|matchExp
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|DataContextDisjointByIdPrefetchTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DataChannelInterceptor
name|queryInterceptor
decl_stmt|;
specifier|protected
name|TableHelper
name|tBag
decl_stmt|;
specifier|protected
name|TableHelper
name|tBox
decl_stmt|;
specifier|protected
name|TableHelper
name|tBoxInfo
decl_stmt|;
specifier|protected
name|TableHelper
name|tBall
decl_stmt|;
specifier|protected
name|TableHelper
name|tThing
decl_stmt|;
specifier|protected
name|TableHelper
name|tBoxThing
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BALL"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BOX_THING"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"THING"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BOX_INFO"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BOX"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BAG"
argument_list|)
expr_stmt|;
name|tBag
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"BAG"
argument_list|)
expr_stmt|;
name|tBag
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
name|tBox
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"BOX"
argument_list|)
expr_stmt|;
name|tBox
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"BAG_ID"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
name|tBoxInfo
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"BOX_INFO"
argument_list|)
expr_stmt|;
name|tBoxInfo
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"BOX_ID"
argument_list|,
literal|"COLOR"
argument_list|)
expr_stmt|;
name|tBall
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"BALL"
argument_list|)
expr_stmt|;
name|tBall
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"BOX_ID"
argument_list|,
literal|"THING_WEIGHT"
argument_list|,
literal|"THING_VOLUME"
argument_list|)
expr_stmt|;
name|tThing
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"THING"
argument_list|)
expr_stmt|;
name|tThing
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"WEIGHT"
argument_list|,
literal|"VOLUME"
argument_list|)
expr_stmt|;
name|tBoxThing
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"BOX_THING"
argument_list|)
expr_stmt|;
name|tBoxThing
operator|.
name|setColumns
argument_list|(
literal|"BOX_ID"
argument_list|,
literal|"THING_WEIGHT"
argument_list|,
literal|"THING_VOLUME"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createBagWithTwoBoxesDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tBag
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"X"
argument_list|)
expr_stmt|;
name|tBox
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"Y"
argument_list|)
expr_stmt|;
name|tBox
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"Z"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createThreeBagsWithPlentyOfBoxesDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tBag
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"bag1"
argument_list|)
expr_stmt|;
name|tBag
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"bag2"
argument_list|)
expr_stmt|;
name|tBag
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"bag3"
argument_list|)
expr_stmt|;
name|tBox
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"box1"
argument_list|)
expr_stmt|;
name|tBox
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"box2"
argument_list|)
expr_stmt|;
name|tBox
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|1
argument_list|,
literal|"box3"
argument_list|)
expr_stmt|;
name|tBox
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|1
argument_list|,
literal|"box4"
argument_list|)
expr_stmt|;
name|tBox
operator|.
name|insert
argument_list|(
literal|5
argument_list|,
literal|1
argument_list|,
literal|"box5"
argument_list|)
expr_stmt|;
name|tBox
operator|.
name|insert
argument_list|(
literal|6
argument_list|,
literal|2
argument_list|,
literal|"box6"
argument_list|)
expr_stmt|;
name|tBox
operator|.
name|insert
argument_list|(
literal|7
argument_list|,
literal|2
argument_list|,
literal|"box7"
argument_list|)
expr_stmt|;
name|tBox
operator|.
name|insert
argument_list|(
literal|8
argument_list|,
literal|3
argument_list|,
literal|"box8"
argument_list|)
expr_stmt|;
name|tBox
operator|.
name|insert
argument_list|(
literal|9
argument_list|,
literal|3
argument_list|,
literal|"box9"
argument_list|)
expr_stmt|;
name|tBox
operator|.
name|insert
argument_list|(
literal|10
argument_list|,
literal|3
argument_list|,
literal|"box10"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createBagWithTwoBoxesAndPlentyOfBallsDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tBag
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"bag1"
argument_list|)
expr_stmt|;
name|tBox
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"big"
argument_list|)
expr_stmt|;
name|tBoxInfo
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"red"
argument_list|)
expr_stmt|;
name|tBox
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"small"
argument_list|)
expr_stmt|;
name|tBoxInfo
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|2
argument_list|,
literal|"green"
argument_list|)
expr_stmt|;
name|tThing
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|10
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|tBoxThing
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|10
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|tBall
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|10
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|tThing
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|20
argument_list|,
literal|20
argument_list|)
expr_stmt|;
name|tBoxThing
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|20
argument_list|,
literal|20
argument_list|)
expr_stmt|;
name|tBall
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|20
argument_list|,
literal|20
argument_list|)
expr_stmt|;
name|tThing
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|30
argument_list|,
literal|30
argument_list|)
expr_stmt|;
name|tBoxThing
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|30
argument_list|,
literal|30
argument_list|)
expr_stmt|;
name|tBall
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|2
argument_list|,
literal|30
argument_list|,
literal|30
argument_list|)
expr_stmt|;
name|tThing
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|40
argument_list|,
literal|40
argument_list|)
expr_stmt|;
name|tBoxThing
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|40
argument_list|,
literal|40
argument_list|)
expr_stmt|;
name|tBall
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|2
argument_list|,
literal|40
argument_list|,
literal|40
argument_list|)
expr_stmt|;
name|tThing
operator|.
name|insert
argument_list|(
literal|5
argument_list|,
literal|10
argument_list|,
literal|20
argument_list|)
expr_stmt|;
name|tBoxThing
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|10
argument_list|,
literal|20
argument_list|)
expr_stmt|;
name|tBall
operator|.
name|insert
argument_list|(
literal|5
argument_list|,
literal|2
argument_list|,
literal|10
argument_list|,
literal|20
argument_list|)
expr_stmt|;
name|tThing
operator|.
name|insert
argument_list|(
literal|6
argument_list|,
literal|30
argument_list|,
literal|40
argument_list|)
expr_stmt|;
name|tBoxThing
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|30
argument_list|,
literal|40
argument_list|)
expr_stmt|;
name|tBall
operator|.
name|insert
argument_list|(
literal|6
argument_list|,
literal|2
argument_list|,
literal|30
argument_list|,
literal|40
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOneToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createBagWithTwoBoxesDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Bag
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|Bag
operator|.
name|BOXES_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|DISJOINT_BY_ID_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Bag
argument_list|>
name|result
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|queryInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
operator|new
name|UnitTestClosure
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|result
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|Bag
name|b1
init|=
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Box
argument_list|>
name|toMany
init|=
operator|(
name|List
argument_list|<
name|Box
argument_list|>
operator|)
name|b1
operator|.
name|readPropertyDirectly
argument_list|(
name|Bag
operator|.
name|BOXES_PROPERTY
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|toMany
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|toMany
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|toMany
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Box
name|b
range|:
name|toMany
control|)
block|{
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|b
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|names
operator|.
name|add
argument_list|(
name|b
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"Y"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"Z"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testManyToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|createBagWithTwoBoxesDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Box
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|Box
operator|.
name|BAG_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|DISJOINT_BY_ID_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Box
argument_list|>
name|result
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|queryInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
operator|new
name|UnitTestClosure
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|result
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|Box
name|b1
init|=
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|b1
operator|.
name|getBag
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|b1
operator|.
name|getBag
argument_list|()
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"X"
argument_list|,
name|b1
operator|.
name|getBag
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFetchLimit
parameter_list|()
throws|throws
name|Exception
block|{
name|createThreeBagsWithPlentyOfBoxesDataSet
argument_list|()
expr_stmt|;
specifier|final
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Bag
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|Bag
operator|.
name|BOXES_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|DISJOINT_BY_ID_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|query
operator|.
name|setFetchLimit
argument_list|(
literal|2
argument_list|)
expr_stmt|;
comment|// There will be only 2 bags in a result. The first bag has 5 boxes and
comment|// the second has 2. So we are expecting exactly 9 snapshots in the data
comment|// row store after performing the query.
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getDataRowCache
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOneToOneRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|createBagWithTwoBoxesAndPlentyOfBallsDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Box
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|Box
operator|.
name|BOX_INFO_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|DISJOINT_BY_ID_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Box
argument_list|>
name|result
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|queryInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
operator|new
name|UnitTestClosure
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|result
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|Box
name|b1
init|=
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|BoxInfo
name|info
init|=
operator|(
name|BoxInfo
operator|)
name|b1
operator|.
name|readPropertyDirectly
argument_list|(
name|Box
operator|.
name|BOX_INFO_PROPERTY
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|info
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"red"
argument_list|,
name|info
operator|.
name|getColor
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|info
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFlattenedRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|createBagWithTwoBoxesAndPlentyOfBallsDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Bag
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|Bag
operator|.
name|BALLS_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|DISJOINT_BY_ID_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Bag
argument_list|>
name|result
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|queryInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
operator|new
name|UnitTestClosure
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|result
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|Bag
name|b1
init|=
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Ball
argument_list|>
name|balls
init|=
operator|(
name|List
argument_list|<
name|Ball
argument_list|>
operator|)
name|b1
operator|.
name|readPropertyDirectly
argument_list|(
name|Bag
operator|.
name|BALLS_PROPERTY
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|balls
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|balls
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|balls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|volumes
init|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Ball
name|b
range|:
name|balls
control|)
block|{
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|b
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|volumes
operator|.
name|add
argument_list|(
name|b
operator|.
name|getThingVolume
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|volumes
operator|.
name|containsAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|10
argument_list|,
literal|20
argument_list|,
literal|30
argument_list|,
literal|40
argument_list|,
literal|20
argument_list|,
literal|40
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLongFlattenedRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|createBagWithTwoBoxesAndPlentyOfBallsDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Bag
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|Bag
operator|.
name|THINGS_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|DISJOINT_BY_ID_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Bag
argument_list|>
name|result
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|queryInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
operator|new
name|UnitTestClosure
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|result
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|Bag
name|b1
init|=
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Thing
argument_list|>
name|things
init|=
operator|(
name|List
argument_list|<
name|Thing
argument_list|>
operator|)
name|b1
operator|.
name|readPropertyDirectly
argument_list|(
name|Bag
operator|.
name|THINGS_PROPERTY
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|things
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|things
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|things
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|volumes
init|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Thing
name|t
range|:
name|things
control|)
block|{
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|t
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|volumes
operator|.
name|add
argument_list|(
name|t
operator|.
name|getVolume
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|volumes
operator|.
name|containsAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|10
argument_list|,
literal|20
argument_list|,
literal|20
argument_list|,
literal|30
argument_list|,
literal|40
argument_list|,
literal|40
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMultiColumnRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|createBagWithTwoBoxesAndPlentyOfBallsDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Ball
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|orQualifier
argument_list|(
name|matchExp
argument_list|(
name|Ball
operator|.
name|THING_VOLUME_PROPERTY
argument_list|,
literal|40
argument_list|)
operator|.
name|andExp
argument_list|(
name|matchExp
argument_list|(
name|Ball
operator|.
name|THING_WEIGHT_PROPERTY
argument_list|,
literal|30
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|.
name|orQualifier
argument_list|(
name|matchExp
argument_list|(
name|Ball
operator|.
name|THING_VOLUME_PROPERTY
argument_list|,
literal|20
argument_list|)
operator|.
name|andExp
argument_list|(
name|matchExp
argument_list|(
name|Ball
operator|.
name|THING_WEIGHT_PROPERTY
argument_list|,
literal|10
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|Ball
operator|.
name|THING_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|DISJOINT_BY_ID_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getDataRowCache
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFlattenedMultiColumnRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|createBagWithTwoBoxesAndPlentyOfBallsDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Box
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|Box
operator|.
name|THINGS_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|DISJOINT_BY_ID_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Box
argument_list|>
name|result
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|queryInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
operator|new
name|UnitTestClosure
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|result
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|Box
name|b1
init|=
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Thing
argument_list|>
name|things
init|=
operator|(
name|List
argument_list|<
name|Thing
argument_list|>
operator|)
name|b1
operator|.
name|readPropertyDirectly
argument_list|(
name|Box
operator|.
name|THINGS_PROPERTY
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|things
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|things
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|things
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|volumes
init|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Thing
name|t
range|:
name|things
control|)
block|{
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|t
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|volumes
operator|.
name|add
argument_list|(
name|t
operator|.
name|getVolume
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|volumes
operator|.
name|containsAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|10
argument_list|,
literal|20
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testJointPrefetchInParent
parameter_list|()
throws|throws
name|Exception
block|{
name|createBagWithTwoBoxesAndPlentyOfBallsDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Box
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|Box
operator|.
name|BALLS_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|Box
operator|.
name|BALLS_PROPERTY
operator|+
literal|"."
operator|+
name|Ball
operator|.
name|THING_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|DISJOINT_BY_ID_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Box
argument_list|>
name|result
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|queryInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
operator|new
name|UnitTestClosure
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|result
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|Box
name|box
init|=
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Ball
argument_list|>
name|balls
init|=
operator|(
name|List
argument_list|<
name|Ball
argument_list|>
operator|)
name|box
operator|.
name|readPropertyDirectly
argument_list|(
name|Box
operator|.
name|BALLS_PROPERTY
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|balls
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|balls
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|balls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Ball
name|ball
init|=
name|balls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Thing
name|thing
init|=
operator|(
name|Thing
operator|)
name|ball
operator|.
name|readPropertyDirectly
argument_list|(
name|Ball
operator|.
name|THING_PROPERTY
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|thing
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|thing
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|10
argument_list|)
argument_list|,
name|thing
operator|.
name|getVolume
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testJointPrefetchInChild
parameter_list|()
throws|throws
name|Exception
block|{
name|createBagWithTwoBoxesAndPlentyOfBallsDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Bag
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|Bag
operator|.
name|BOXES_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|DISJOINT_BY_ID_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|Bag
operator|.
name|BOXES_PROPERTY
operator|+
literal|"."
operator|+
name|Box
operator|.
name|BALLS_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Bag
argument_list|>
name|result
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|queryInterceptor
operator|.
name|runWithQueriesBlocked
argument_list|(
operator|new
name|UnitTestClosure
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|result
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|Bag
name|bag
init|=
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Box
argument_list|>
name|boxes
init|=
operator|(
name|List
argument_list|<
name|Box
argument_list|>
operator|)
name|bag
operator|.
name|readPropertyDirectly
argument_list|(
name|Bag
operator|.
name|BOXES_PROPERTY
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|boxes
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|boxes
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|boxes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Box
name|box
range|:
name|boxes
control|)
block|{
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|box
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|names
operator|.
name|add
argument_list|(
name|box
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"big"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"small"
argument_list|)
argument_list|)
expr_stmt|;
name|Box
name|box
init|=
name|boxes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Ball
argument_list|>
name|balls
init|=
operator|(
name|List
argument_list|<
name|Ball
argument_list|>
operator|)
name|box
operator|.
name|readPropertyDirectly
argument_list|(
name|Box
operator|.
name|BALLS_PROPERTY
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|balls
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|balls
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|balls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|volumes
init|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Ball
name|ball
range|:
name|balls
control|)
block|{
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|ball
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|volumes
operator|.
name|add
argument_list|(
name|ball
operator|.
name|getThingVolume
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|volumes
operator|.
name|containsAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|10
argument_list|,
literal|20
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

