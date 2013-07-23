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
name|inherit
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
name|testdo
operator|.
name|inherit
operator|.
name|Employee
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
name|inherit
operator|.
name|Manager
import|;
end_import

begin_comment
comment|/**  * Class _Department was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Department
extends|extends
name|CayenneDataObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|NAME_PROPERTY
init|=
literal|"name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EMPLOYEES_PROPERTY
init|=
literal|"employees"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TO_MANAGER_PROPERTY
init|=
literal|"toManager"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEPARTMENT_ID_PK_COLUMN
init|=
literal|"DEPARTMENT_ID"
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
name|NAME_PROPERTY
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
name|NAME_PROPERTY
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToEmployees
parameter_list|(
name|Employee
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
name|EMPLOYEES_PROPERTY
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromEmployees
parameter_list|(
name|Employee
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
name|EMPLOYEES_PROPERTY
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
name|Employee
argument_list|>
name|getEmployees
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|Employee
argument_list|>
operator|)
name|readProperty
argument_list|(
name|EMPLOYEES_PROPERTY
argument_list|)
return|;
block|}
specifier|public
name|void
name|setToManager
parameter_list|(
name|Manager
name|toManager
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
name|TO_MANAGER_PROPERTY
argument_list|,
name|toManager
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Manager
name|getToManager
parameter_list|()
block|{
return|return
operator|(
name|Manager
operator|)
name|readProperty
argument_list|(
name|TO_MANAGER_PROPERTY
argument_list|)
return|;
block|}
block|}
end_class

end_unit
