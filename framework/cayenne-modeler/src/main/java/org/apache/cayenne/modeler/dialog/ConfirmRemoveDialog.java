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
name|dialog
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
name|JDialog
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JOptionPane
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
name|modeler
operator|.
name|Application
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
name|modeler
operator|.
name|dialog
operator|.
name|pref
operator|.
name|GeneralPreferences
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
name|pref
operator|.
name|PreferenceDetail
import|;
end_import

begin_comment
comment|/**  * Used to confirm deleting items in the model.  *  * @author Kevin Menard  */
end_comment

begin_class
specifier|public
class|class
name|ConfirmRemoveDialog
block|{
specifier|private
name|boolean
name|shouldDelete
init|=
literal|true
decl_stmt|;
specifier|private
name|void
name|showDialog
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|JCheckBox
name|neverPromptAgainBox
init|=
operator|new
name|JCheckBox
argument_list|(
literal|"Always delete without prompt."
argument_list|)
decl_stmt|;
name|Object
name|message
index|[]
init|=
block|{
name|String
operator|.
name|format
argument_list|(
literal|"Are you sure you would like to delete the %s named '%s'?"
argument_list|,
name|type
argument_list|,
name|name
argument_list|)
block|,
name|neverPromptAgainBox
block|}
decl_stmt|;
name|JOptionPane
name|pane
init|=
operator|new
name|JOptionPane
argument_list|(
name|message
argument_list|,
name|JOptionPane
operator|.
name|QUESTION_MESSAGE
argument_list|,
name|JOptionPane
operator|.
name|YES_NO_OPTION
argument_list|)
decl_stmt|;
name|JDialog
name|dialog
init|=
name|pane
operator|.
name|createDialog
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
literal|"Confirm Remove"
argument_list|)
decl_stmt|;
name|dialog
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Object
name|selectedValue
init|=
name|pane
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|shouldDelete
operator|=
name|selectedValue
operator|.
name|equals
argument_list|(
name|JOptionPane
operator|.
name|YES_OPTION
argument_list|)
expr_stmt|;
comment|// If the user clicks "no", we'll just ignore whatever's in the checkbox because it's non-sensical.
if|if
condition|(
name|shouldDelete
condition|)
block|{
name|PreferenceDetail
name|pref
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getPreferenceDomain
argument_list|()
operator|.
name|getDetail
argument_list|(
name|GeneralPreferences
operator|.
name|DELETE_PROMPT_PREFERENCE
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|pref
operator|.
name|setBooleanProperty
argument_list|(
name|GeneralPreferences
operator|.
name|DELETE_PROMPT_PREFERENCE
argument_list|,
name|neverPromptAgainBox
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getPreferenceService
argument_list|()
operator|.
name|savePreferences
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|shouldDelete
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|PreferenceDetail
name|pref
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getPreferenceDomain
argument_list|()
operator|.
name|getDetail
argument_list|(
name|GeneralPreferences
operator|.
name|DELETE_PROMPT_PREFERENCE
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// See if the user has opted not to showDialog the delete dialog.
if|if
condition|(
operator|(
name|pref
operator|==
literal|null
operator|)
operator|||
operator|(
literal|false
operator|==
name|pref
operator|.
name|getBooleanProperty
argument_list|(
name|GeneralPreferences
operator|.
name|DELETE_PROMPT_PREFERENCE
argument_list|)
operator|)
condition|)
block|{
name|showDialog
argument_list|(
name|type
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|shouldDelete
return|;
block|}
block|}
end_class

end_unit

