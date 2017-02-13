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
name|relationships_activity
operator|.
name|auto
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Date
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
name|exp
operator|.
name|Property
import|;
end_import

begin_comment
comment|/**  * Class _ActivityResult was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_ActivityResult
extends|extends
name|CayenneDataObject
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
name|APPOINT_DATE_PK_COLUMN
init|=
literal|"APPOINT_DATE"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|APPOINT_NO_PK_COLUMN
init|=
literal|"APPOINT_NO"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RESULTNAME_PK_COLUMN
init|=
literal|"RESULTNAME"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Date
argument_list|>
name|APPOINT_DATE
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"appointDate"
argument_list|,
name|Date
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Integer
argument_list|>
name|APPOINT_NO
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"appointNo"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|FIELD
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"field"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setAppointDate
parameter_list|(
name|Date
name|appointDate
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"appointDate"
argument_list|,
name|appointDate
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Date
name|getAppointDate
parameter_list|()
block|{
return|return
operator|(
name|Date
operator|)
name|readProperty
argument_list|(
literal|"appointDate"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setAppointNo
parameter_list|(
name|int
name|appointNo
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"appointNo"
argument_list|,
name|appointNo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getAppointNo
parameter_list|()
block|{
name|Object
name|value
init|=
name|readProperty
argument_list|(
literal|"appointNo"
argument_list|)
decl_stmt|;
return|return
operator|(
name|value
operator|!=
literal|null
operator|)
condition|?
operator|(
name|Integer
operator|)
name|value
else|:
literal|0
return|;
block|}
specifier|public
name|void
name|setField
parameter_list|(
name|String
name|field
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"field"
argument_list|,
name|field
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getField
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"field"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

