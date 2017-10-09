package tatai.ui.page;

public class PronunciationPage extends Page {

    public PronunciationPage() {
        loadFXML(getClass().getResource("Pronunciation.fxml"));
    }

    @Override
    public String getTitle() {
        return "Title Here";
    }

    @Override
    public void onBackButtonPressed() {

    }

    @Override
    public void onOptionsButtonPressed() {

    }
}
