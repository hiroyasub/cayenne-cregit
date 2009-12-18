begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|//
end_comment

begin_comment
comment|// DirectAction.java
end_comment

begin_comment
comment|// Project wotests
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Created by andrus on Sun Dec 21 2003
end_comment

begin_comment
comment|//
end_comment

begin_import
import|import
name|com
operator|.
name|webobjects
operator|.
name|foundation
operator|.
name|*
import|;
end_import

begin_import
import|import
name|com
operator|.
name|webobjects
operator|.
name|appserver
operator|.
name|*
import|;
end_import

begin_import
import|import
name|com
operator|.
name|webobjects
operator|.
name|eocontrol
operator|.
name|*
import|;
end_import

begin_class
specifier|public
class|class
name|DirectAction
extends|extends
name|WODirectAction
block|{
specifier|public
name|DirectAction
parameter_list|(
name|WORequest
name|aRequest
parameter_list|)
block|{
name|super
argument_list|(
name|aRequest
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WOActionResults
name|defaultAction
parameter_list|()
block|{
return|return
name|pageWithName
argument_list|(
literal|"Main"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

