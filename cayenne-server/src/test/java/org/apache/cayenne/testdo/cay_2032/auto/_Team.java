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
name|cay_2032
operator|.
name|auto
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
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
name|BaseDataObject
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
name|cay_2032
operator|.
name|User
import|;
end_import

begin_comment
comment|/**  * Class _Team was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Team
extends|extends
name|BaseDataObject
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TEAM_ID_PK_COLUMN
init|=
literal|"team_id"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|List
argument_list|<
name|User
argument_list|>
argument_list|>
name|TEAM_USERS
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"teamUsers"
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|Object
name|teamUsers
decl_stmt|;
specifier|public
name|void
name|addToTeamUsers
parameter_list|(
name|User
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"teamUsers"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromTeamUsers
parameter_list|(
name|User
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"teamUsers"
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
name|User
argument_list|>
name|getTeamUsers
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|User
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"teamUsers"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|readPropertyDirectly
parameter_list|(
name|String
name|propName
parameter_list|)
block|{
if|if
condition|(
name|propName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
switch|switch
condition|(
name|propName
condition|)
block|{
case|case
literal|"teamUsers"
case|:
return|return
name|this
operator|.
name|teamUsers
return|;
default|default:
return|return
name|super
operator|.
name|readPropertyDirectly
argument_list|(
name|propName
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|writePropertyDirectly
parameter_list|(
name|String
name|propName
parameter_list|,
name|Object
name|val
parameter_list|)
block|{
if|if
condition|(
name|propName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
switch|switch
condition|(
name|propName
condition|)
block|{
case|case
literal|"teamUsers"
case|:
name|this
operator|.
name|teamUsers
operator|=
name|val
expr_stmt|;
break|break;
default|default:
name|super
operator|.
name|writePropertyDirectly
argument_list|(
name|propName
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|writeObject
parameter_list|(
name|ObjectOutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|writeSerialized
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|readObject
parameter_list|(
name|ObjectInputStream
name|in
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|readSerialized
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|writeState
parameter_list|(
name|ObjectOutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|writeState
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|teamUsers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|readState
parameter_list|(
name|ObjectInputStream
name|in
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|super
operator|.
name|readState
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|this
operator|.
name|teamUsers
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

