begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|itest
operator|.
name|cpa
operator|.
name|exp
package|;
end_package

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
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|Expression
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
name|ExpressionFactory
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
name|itest
operator|.
name|cpa
operator|.
name|CPAContextCase
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
name|itest
operator|.
name|cpa
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
name|itest
operator|.
name|cpa
operator|.
name|EnumEntity1
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

begin_comment
comment|// inspired by CAY-832
end_comment

begin_class
specifier|public
class|class
name|InExpressionTest
extends|extends
name|CPAContextCase
block|{
specifier|public
name|void
name|testInEnumsMappedAsChar
parameter_list|()
throws|throws
name|Exception
block|{
name|getDbHelper
argument_list|()
operator|.
name|deleteAll
argument_list|(
literal|"enum_entity1"
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|insert
argument_list|(
literal|"enum_entity1"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"char_enum"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|1
block|,
literal|"One"
block|}
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|insert
argument_list|(
literal|"enum_entity1"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"char_enum"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|2
block|,
literal|"Two"
block|}
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|insert
argument_list|(
literal|"enum_entity1"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"char_enum"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|3
block|,
literal|"Three"
block|}
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Enum1
argument_list|>
name|enums
init|=
operator|new
name|ArrayList
argument_list|<
name|Enum1
argument_list|>
argument_list|()
decl_stmt|;
name|enums
operator|.
name|add
argument_list|(
name|Enum1
operator|.
name|Two
argument_list|)
expr_stmt|;
name|enums
operator|.
name|add
argument_list|(
name|Enum1
operator|.
name|Four
argument_list|)
expr_stmt|;
name|Expression
name|charMatch
init|=
name|ExpressionFactory
operator|.
name|inExp
argument_list|(
name|EnumEntity1
operator|.
name|CHAR_ENUM_PROPERTY
argument_list|,
name|enums
argument_list|)
decl_stmt|;
name|List
name|results
init|=
name|getContext
argument_list|()
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|EnumEntity1
operator|.
name|class
argument_list|,
name|charMatch
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|EnumEntity1
name|o1
init|=
operator|(
name|EnumEntity1
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|Enum1
operator|.
name|Two
argument_list|,
name|o1
operator|.
name|getCharEnum
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInEnumsMappedAsInt
parameter_list|()
throws|throws
name|Exception
block|{
name|getDbHelper
argument_list|()
operator|.
name|deleteAll
argument_list|(
literal|"enum_entity1"
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|insert
argument_list|(
literal|"enum_entity1"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"int_enum"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|1
block|,
literal|0
block|}
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|insert
argument_list|(
literal|"enum_entity1"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"int_enum"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|2
block|,
literal|1
block|}
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|insert
argument_list|(
literal|"enum_entity1"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"int_enum"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|3
block|,
literal|2
block|}
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Enum1
argument_list|>
name|enums
init|=
operator|new
name|ArrayList
argument_list|<
name|Enum1
argument_list|>
argument_list|()
decl_stmt|;
name|enums
operator|.
name|add
argument_list|(
name|Enum1
operator|.
name|Two
argument_list|)
expr_stmt|;
name|enums
operator|.
name|add
argument_list|(
name|Enum1
operator|.
name|Four
argument_list|)
expr_stmt|;
name|Expression
name|charMatch
init|=
name|ExpressionFactory
operator|.
name|inExp
argument_list|(
name|EnumEntity1
operator|.
name|INT_ENUM_PROPERTY
argument_list|,
name|enums
argument_list|)
decl_stmt|;
name|List
name|results
init|=
name|getContext
argument_list|()
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|EnumEntity1
operator|.
name|class
argument_list|,
name|charMatch
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|EnumEntity1
name|o1
init|=
operator|(
name|EnumEntity1
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|Enum1
operator|.
name|Two
argument_list|,
name|o1
operator|.
name|getIntEnum
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInEnumsMappedAsIntFromString
parameter_list|()
throws|throws
name|Exception
block|{
name|getDbHelper
argument_list|()
operator|.
name|deleteAll
argument_list|(
literal|"enum_entity1"
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|insert
argument_list|(
literal|"enum_entity1"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"int_enum"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|1
block|,
literal|0
block|}
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|insert
argument_list|(
literal|"enum_entity1"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"int_enum"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|2
block|,
literal|1
block|}
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|insert
argument_list|(
literal|"enum_entity1"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"int_enum"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|3
block|,
literal|2
block|}
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Enum1
argument_list|>
name|enums
init|=
operator|new
name|ArrayList
argument_list|<
name|Enum1
argument_list|>
argument_list|()
decl_stmt|;
name|enums
operator|.
name|add
argument_list|(
name|Enum1
operator|.
name|Two
argument_list|)
expr_stmt|;
name|enums
operator|.
name|add
argument_list|(
name|Enum1
operator|.
name|Four
argument_list|)
expr_stmt|;
name|Expression
name|charMatch
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"intEnum in $l"
argument_list|)
operator|.
name|expWithParameters
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"l"
argument_list|,
name|enums
argument_list|)
argument_list|)
decl_stmt|;
name|List
name|results
init|=
name|getContext
argument_list|()
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|EnumEntity1
operator|.
name|class
argument_list|,
name|charMatch
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|EnumEntity1
name|o1
init|=
operator|(
name|EnumEntity1
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|Enum1
operator|.
name|Two
argument_list|,
name|o1
operator|.
name|getIntEnum
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

