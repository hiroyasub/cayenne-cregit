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
name|CustomerRepresentative
import|;
end_import

begin_comment
comment|/**  * Class _ClientCompany was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_ClientCompany
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
name|REPRESENTATIVES_PROPERTY
init|=
literal|"representatives"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_COMPANY_ID_PK_COLUMN
init|=
literal|"CLIENT_COMPANY_ID"
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
name|addToRepresentatives
parameter_list|(
name|CustomerRepresentative
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
name|REPRESENTATIVES_PROPERTY
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromRepresentatives
parameter_list|(
name|CustomerRepresentative
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
name|REPRESENTATIVES_PROPERTY
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
name|CustomerRepresentative
argument_list|>
name|getRepresentatives
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|CustomerRepresentative
argument_list|>
operator|)
name|readProperty
argument_list|(
name|REPRESENTATIVES_PROPERTY
argument_list|)
return|;
block|}
block|}
end_class

end_unit
