begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|//
end_comment

begin_comment
comment|// Application.java
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
name|Application
extends|extends
name|WOApplication
block|{
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|argv
index|[]
parameter_list|)
block|{
name|WOApplication
operator|.
name|main
argument_list|(
name|argv
argument_list|,
name|Application
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Application
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Welcome to "
operator|+
name|this
operator|.
name|name
argument_list|()
operator|+
literal|"!"
argument_list|)
expr_stmt|;
comment|/* ** Put your application initialization code here ** */
block|}
block|}
end_class

end_unit

