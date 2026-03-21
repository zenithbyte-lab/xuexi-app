/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{vue,js,ts}'],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        'surface-container-highest': '#d6dbff',
        'surface-container-lowest': '#ffffff',
        'on-secondary-container': '#223ea2',
        'tertiary-fixed-dim': '#5bf083',
        'surface-variant': '#d6dbff',
        'on-tertiary-fixed-variant': '#006a2d',
        'on-error-container': '#570008',
        'secondary-dim': '#2c47ab',
        'surface-tint': '#0057bd',
        'surface-dim': '#cad2ff',
        'primary': '#0057bd',
        'on-error': '#ffefee',
        'inverse-surface': '#020a2f',
        'tertiary-dim': '#005d26',
        'outline': '#6c759e',
        'secondary-fixed-dim': '#b4c1ff',
        'background': '#f7f5ff',
        'primary-fixed-dim': '#5391ff',
        'inverse-primary': '#4d8eff',
        'primary-fixed': '#6e9fff',
        'on-primary-container': '#002150',
        'on-secondary-fixed-variant': '#2d48ac',
        'on-background': '#242c51',
        'on-surface-variant': '#515981',
        'secondary': '#3a53b7',
        'inverse-on-surface': '#929bc6',
        'surface-container': '#e4e7ff',
        'on-primary': '#f0f2ff',
        'on-tertiary-fixed': '#004a1d',
        'on-secondary-fixed': '#00278f',
        'surface-bright': '#f7f5ff',
        'error-container': '#fb5151',
        'tertiary': '#006a2d',
        'error': '#b31b25',
        'surface-container-high': '#dde1ff',
        'surface-container-low': '#efefff',
        'on-secondary': '#f2f1ff',
        'error-dim': '#9f0519',
        'tertiary-fixed': '#6bff8f',
        'surface': '#f7f5ff',
        'on-surface': '#242c51',
        'secondary-container': '#c7cfff',
        'on-tertiary': '#ceffd0',
        'tertiary-container': '#6bff8f',
        'on-primary-fixed-variant': '#002a62',
        'primary-dim': '#004ca6',
        'on-primary-fixed': '#000000',
        'secondary-fixed': '#c7cfff',
        'primary-container': '#6e9fff',
        'outline-variant': '#a3abd7',
        'on-tertiary-container': '#005f28'
      },
      fontFamily: {
        'headline': ['Plus Jakarta Sans', 'sans-serif'],
        'body': ['Inter', 'sans-serif'],
        'label': ['Inter', 'sans-serif']
      },
      borderRadius: {
        DEFAULT: '1rem',
        lg: '2rem',
        xl: '3rem',
        full: '9999px'
      }
    }
  },
  plugins: []
}
