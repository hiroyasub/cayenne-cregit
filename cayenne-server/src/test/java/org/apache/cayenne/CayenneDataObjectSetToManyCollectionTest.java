begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
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
name|Collection
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
name|relationships_collection_to_many
operator|.
name|CollectionToMany
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
name|relationships_collection_to_many
operator|.
name|CollectionToManyTarget
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
name|CayenneProjects
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
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|RELATIONSHIPS_COLLECTION_TO_MANY_PROJECT
argument_list|)
specifier|public
class|class
name|CayenneDataObjectSetToManyCollectionTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|TableHelper
name|tCollectionToMany
init|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"COLLECTION_TO_MANY"
argument_list|)
decl_stmt|;
name|tCollectionToMany
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|)
expr_stmt|;
name|TableHelper
name|tCollectionToManyTarget
init|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"COLLECTION_TO_MANY_TARGET"
argument_list|)
decl_stmt|;
name|tCollectionToManyTarget
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"COLLECTION_TO_MANY_ID"
argument_list|)
expr_stmt|;
comment|// single data set for all tests
name|tCollectionToMany
operator|.
name|insert
argument_list|(
literal|1
argument_list|)
operator|.
name|insert
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|tCollectionToManyTarget
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|1
argument_list|)
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|CollectionToMany
name|o1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|CollectionToMany
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|?
argument_list|>
name|targets
init|=
name|o1
operator|.
name|getTargets
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|targets
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|targets
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|targets
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|targets
operator|.
name|contains
argument_list|(
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|,
name|CollectionToManyTarget
operator|.
name|class
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|targets
operator|.
name|contains
argument_list|(
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|,
name|CollectionToManyTarget
operator|.
name|class
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|targets
operator|.
name|contains
argument_list|(
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|,
name|CollectionToManyTarget
operator|.
name|class
argument_list|,
literal|3
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Testing if collection type is Collection, everything should work fine without an 	 * runtimexception 	 *  	 * @throws Exception 	 */
annotation|@
name|Test
specifier|public
name|void
name|testRelationCollectionTypeCollection
parameter_list|()
throws|throws
name|Exception
block|{
name|CollectionToMany
name|o1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|CollectionToMany
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|o1
operator|.
name|readProperty
argument_list|(
name|CollectionToMany
operator|.
name|TARGETS_PROPERTY
argument_list|)
operator|instanceof
name|Collection
argument_list|)
expr_stmt|;
name|boolean
name|catchedSomething
init|=
literal|false
decl_stmt|;
try|try
block|{
name|o1
operator|.
name|setToManyTarget
argument_list|(
name|CollectionToMany
operator|.
name|TARGETS_PROPERTY
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|CollectionToMany
argument_list|>
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|catchedSomething
operator|=
literal|true
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|catchedSomething
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o1
operator|.
name|getTargets
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

