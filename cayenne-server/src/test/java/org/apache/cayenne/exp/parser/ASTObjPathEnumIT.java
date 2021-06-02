begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|parser
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
name|ObjectContext
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
name|testdo
operator|.
name|enum_test
operator|.
name|Enum1
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
name|enum_test
operator|.
name|EnumEntity
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
name|Test
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
name|assertNull
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|ENUM_PROJECT
argument_list|)
specifier|public
class|class
name|ASTObjPathEnumIT
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
name|Test
specifier|public
name|void
name|testInjectEnumByName
parameter_list|()
block|{
name|ASTObjPath
name|node
init|=
operator|new
name|ASTObjPath
argument_list|(
literal|"enumAttribute"
argument_list|)
decl_stmt|;
name|EnumEntity
name|enumEntity
init|=
name|context
operator|.
name|newObject
argument_list|(
name|EnumEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|enumEntity
operator|.
name|getEnumAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|node
operator|.
name|injectValue
argument_list|(
name|enumEntity
argument_list|,
literal|"one"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Enum1
operator|.
name|one
argument_list|,
name|enumEntity
operator|.
name|getEnumAttribute
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testInjectUnknownEnumByName
parameter_list|()
block|{
name|ASTObjPath
name|node
init|=
operator|new
name|ASTObjPath
argument_list|(
literal|"enumAttribute"
argument_list|)
decl_stmt|;
name|EnumEntity
name|enumEntity
init|=
name|context
operator|.
name|newObject
argument_list|(
name|EnumEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|enumEntity
operator|.
name|getEnumAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|node
operator|.
name|injectValue
argument_list|(
name|enumEntity
argument_list|,
literal|"four"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

