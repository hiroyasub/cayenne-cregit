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
name|editor
operator|.
name|cgen
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JCheckBox
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|UIManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|table
operator|.
name|JTableHeader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|table
operator|.
name|TableCellRenderer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|MouseEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|MouseListener
import|;
end_import

begin_class
class|class
name|CheckBoxHeader
extends|extends
name|JCheckBox
implements|implements
name|TableCellRenderer
implements|,
name|MouseListener
block|{
specifier|protected
name|int
name|column
decl_stmt|;
specifier|protected
name|boolean
name|mousePressed
init|=
literal|false
decl_stmt|;
specifier|private
specifier|final
name|CheckBoxHeader
name|rendererComponent
decl_stmt|;
specifier|public
name|CheckBoxHeader
parameter_list|()
block|{
name|this
operator|.
name|rendererComponent
operator|=
name|this
expr_stmt|;
block|}
specifier|public
name|Component
name|getTableCellRendererComponent
parameter_list|(
name|JTable
name|table
parameter_list|,
name|Object
name|value
parameter_list|,
name|boolean
name|isSelected
parameter_list|,
name|boolean
name|hasFocus
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
name|this
operator|.
name|column
operator|=
name|column
expr_stmt|;
if|if
condition|(
name|table
operator|!=
literal|null
condition|)
block|{
name|JTableHeader
name|header
init|=
name|table
operator|.
name|getTableHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|header
operator|!=
literal|null
condition|)
block|{
name|header
operator|.
name|addMouseListener
argument_list|(
name|rendererComponent
argument_list|)
expr_stmt|;
block|}
block|}
name|rendererComponent
operator|.
name|setBackground
argument_list|(
name|UIManager
operator|.
name|getColor
argument_list|(
literal|"Table.selectionBackground"
argument_list|)
argument_list|)
expr_stmt|;
name|setBorder
argument_list|(
name|UIManager
operator|.
name|getBorder
argument_list|(
literal|"CheckBoxHeader.border"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|rendererComponent
return|;
block|}
specifier|public
name|void
name|mouseClicked
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|mousePressed
condition|)
block|{
name|mousePressed
operator|=
literal|false
expr_stmt|;
name|JTableHeader
name|header
init|=
operator|(
name|JTableHeader
operator|)
operator|(
name|e
operator|.
name|getSource
argument_list|()
operator|)
decl_stmt|;
name|int
name|columnAtPoint
init|=
name|header
operator|.
name|columnAtPoint
argument_list|(
name|e
operator|.
name|getPoint
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|columnAtPoint
operator|==
name|column
condition|)
block|{
name|doClick
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|mousePressed
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
name|mousePressed
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|mouseReleased
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|mouseEntered
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|mouseExited
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
block|}
block|}
end_class

end_unit

