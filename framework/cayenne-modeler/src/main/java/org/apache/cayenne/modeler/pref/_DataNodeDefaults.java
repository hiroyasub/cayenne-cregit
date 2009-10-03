begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|pref
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
name|pref
operator|.
name|PreferenceDetail
import|;
end_import

begin_comment
comment|/**  * Class _DataNodeDefaults was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_DataNodeDefaults
extends|extends
name|PreferenceDetail
block|{
specifier|public
specifier|static
specifier|final
name|String
name|LOCAL_DATA_SOURCE_PROPERTY
init|=
literal|"localDataSource"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ID_PK_COLUMN
init|=
literal|"id"
decl_stmt|;
specifier|public
name|void
name|setLocalDataSource
parameter_list|(
name|String
name|localDataSource
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"localDataSource"
argument_list|,
name|localDataSource
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getLocalDataSource
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"localDataSource"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

