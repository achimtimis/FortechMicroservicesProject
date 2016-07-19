import { bootstrap } from '@angular/platform-browser-dynamic';
import { disableDeprecatedForms, provideForms } from '@angular/forms';

// Our main component
import { AppComponent } from './app.component';

bootstrap(AppComponent, [
    disableDeprecatedForms(),
    provideForms()
]);