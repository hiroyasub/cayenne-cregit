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

begin_comment
comment|/** Class _DBConnectionInfo was generated by Cayenne.   * It is probably a good idea to avoid changing this class manually,    * since it may be overwritten next time code is regenerated.    * If you need to make any customizations, please use subclass.    */
end_comment

begin_class
specifier|public
class|class
name|_DBConnectionInfo
extends|extends
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|pref
operator|.
name|PreferenceDetail
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DB_ADAPTER_PROPERTY
init|=
literal|"dbAdapter"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DOMAIN_PREFERENCE_ID_PROPERTY
init|=
literal|"domainPreferenceId"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JDBC_DRIVER_PROPERTY
init|=
literal|"jdbcDriver"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PASSWORD_PROPERTY
init|=
literal|"password"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|URL_PROPERTY
init|=
literal|"url"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|USER_NAME_PROPERTY
init|=
literal|"userName"
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
name|setDbAdapter
parameter_list|(
name|String
name|dbAdapter
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"dbAdapter"
argument_list|,
name|dbAdapter
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getDbAdapter
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"dbAdapter"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setDomainPreferenceId
parameter_list|(
name|Integer
name|domainPreferenceId
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"domainPreferenceId"
argument_list|,
name|domainPreferenceId
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Integer
name|getDomainPreferenceId
parameter_list|()
block|{
return|return
operator|(
name|Integer
operator|)
name|readProperty
argument_list|(
literal|"domainPreferenceId"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setJdbcDriver
parameter_list|(
name|String
name|jdbcDriver
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"jdbcDriver"
argument_list|,
name|jdbcDriver
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getJdbcDriver
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"jdbcDriver"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"password"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"url"
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"url"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"userName"
argument_list|,
name|userName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"userName"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

