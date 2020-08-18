import './App.css';
import React from 'react';
import {MainPage} from "./MainPage";


// here app catches the suspense from page in case translations are not yet loaded
export default function App() {
  return React.createElement(MainPage);
}
